package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.voucherResponse.UserVoucherResponse;
import com.salespage.salespageservice.app.responses.voucherResponse.VoucherCodeResponse;
import com.salespage.salespageservice.domains.entities.*;
import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import com.salespage.salespageservice.domains.entities.status.VoucherCodeStatus;
import com.salespage.salespageservice.domains.entities.status.VoucherStoreStatus;
import com.salespage.salespageservice.domains.entities.types.DiscountType;
import com.salespage.salespageservice.domains.entities.types.VoucherStoreType;
import com.salespage.salespageservice.domains.exceptions.BadRequestException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.exceptions.TransactionException;
import com.salespage.salespageservice.domains.exceptions.VoucherCodeException;
import com.salespage.salespageservice.domains.exceptions.info.ErrorCode;
import com.salespage.salespageservice.domains.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VoucherCodeService extends BaseService {

    @Autowired
    @Lazy
    private VoucherStoreService voucherStoreService;


    public void deleteAllVoucherCodeInStore() {

    }

    @Transactional
    public void generateVoucherCode(String username, String voucherStoreId, Long numberVoucher, Date expireTime) {
        voucherStoreService.updateQuantityOfVoucherStore(voucherStoreId, 0L, numberVoucher, username);
        List<VoucherCode> voucherCodes = new ArrayList<>();
        for (int i = 0; i < numberVoucher; i++) {
            VoucherCode voucherCode = new VoucherCode();
            voucherCode.setVoucherStoreId(voucherStoreId);
            voucherCode.setExpireTime(expireTime);
            voucherCode.setCode(Helper.generateRandomString());
            voucherCodes.add(voucherCode);
        }
        voucherCodeStorage.saveAll(voucherCodes);
    }

    @Transactional
    public String receiveVoucher(String username, String voucherStoreId) {
        VoucherStore voucherStore = voucherStoreStorage.findVoucherStoreById(voucherStoreId);

        if (Objects.isNull(voucherStore) || !voucherStore.getVoucherStoreStatus().equals(VoucherStoreStatus.ACTIVE))
            throw new ResourceNotFoundException("Mã giảm giá hiện đã bị ngưng sử dụng");

        VoucherCodeLimit voucherCodeLimit = voucherCodeLimitStorage.findByUsernameAndVoucherStoreId(username, voucherStoreId);
        if (Objects.isNull(voucherCodeLimit)) {
            voucherCodeLimit = new VoucherCodeLimit();
            voucherCodeLimit.setUsername(username);
            voucherCodeLimit.setVoucherStoreId(voucherStoreId);
            voucherCodeLimit.setNumberReceiveVoucher(0L);
        }
        voucherCodeLimit.setNumberReceiveVoucher(voucherCodeLimit.getNumberReceiveVoucher() + 1);
        if (voucherCodeLimit.getNumberReceiveVoucher() > voucherStore.getVoucherStoreDetail().getMaxVoucherPerUser())
            throw new VoucherCodeException(ErrorCode.LIMIT_RECEIVE_VOUCHER, "Bạn đã nhận tối đa số lượng mã giảm giá");
        VoucherCode voucherCode = voucherCodeStorage.findFirstVoucherCanUseByVoucherStoreId(voucherStoreId, new Date());
        voucherCode.setOwnerId(username);
        voucherCode.setVoucherCodeStatus(VoucherCodeStatus.OWNER);
        voucherStore.getVoucherStoreDetail().setQuantityUsed(voucherStore.getVoucherStoreDetail().getQuantityUsed() + 1);
        voucherCodeStorage.save(voucherCode);
        voucherCodeLimitStorage.save(voucherCodeLimit);
        voucherStoreStorage.save(voucherStore);
        return voucherCode.getCode();
    }

    public VoucherInfo useVoucher(String username, String code, ProductTransaction productTransaction, String storeId, Double price) {
        VoucherInfo voucherInfo = new VoucherInfo();
        voucherInfo.setPriceBefore(productTransaction.getTotalPrice());
        VoucherCode voucherCode = voucherCodeStorage.findCodeCanUse(username, code);
        if (Objects.isNull(voucherCode)) throw new ResourceNotFoundException("Mã giảm giá không hợp lệ");
        if (voucherCode.getExpireTime().before(new Date()))
            throw new TransactionException(ErrorCode.EXPIRE_VOUCHER, "Mã giảm giá đã hết hạn");

        VoucherStore voucherStore = voucherStoreStorage.findVoucherStoreById(voucherCode.getVoucherStoreId());

        if (Objects.isNull(voucherStore) || !voucherStore.getVoucherStoreStatus().equals(VoucherStoreStatus.ACTIVE))
            throw new ResourceNotFoundException("Mã giảm giá hiện đã bị ngưng sử dụng");

        if (voucherStore.getVoucherStoreDetail().getMaxAblePrice() < price || voucherStore.getVoucherStoreDetail().getMinAblePrice() > price)
            throw new BadRequestException("Mã không thể sử dụng cho sản phẩm này, không nằm trong giá trị mã có thể sử dụng");

        if (voucherStore.getVoucherStoreType() == VoucherStoreType.PRODUCT) {
            if (!voucherStore.getRefId().equals(productTransaction.getProductId())) throw new  BadRequestException("Mã giảm giá không áp dụng cho sản phẩm này");
        } else{
            if (!voucherStore.getRefId().equals(storeId)) throw new  BadRequestException("Mã giảm giá không áp dụng cho cửa hàng này");
        }

        if(voucherStore.getDiscountType().equals(DiscountType.PERCENT)){
            productTransaction.setTotalPrice(productTransaction.getTotalPrice() * (voucherStore.getValue()/100));
        }else{
            productTransaction.setTotalPrice(productTransaction.getTotalPrice() - voucherStore.getValue());
        }

        voucherCode.setUserAt(new Date());
        voucherCode.setVoucherCodeStatus(VoucherCodeStatus.USED);
        voucherCodeStorage.save(voucherCode);
        voucherInfo.setVoucherCode(code);
        voucherInfo.setVoucherStoreType(voucherStore.getVoucherStoreType());
        voucherInfo.setPriceAfter(productTransaction.getTotalPrice());
        voucherInfo.setTotalDiscount(voucherInfo.getPriceAfter() - voucherInfo.getPriceBefore());
        return voucherInfo;
    }

    public PageResponse getAllVoucherCodeInStore(String username, String voucherStoreId, VoucherCodeStatus voucherCodeStatus, Pageable pageable) {
        VoucherStore voucherStore = voucherStoreStorage.findVoucherStoreById(voucherStoreId);
        if (Objects.isNull(voucherStore))
            throw new  BadRequestException( "Cửa hàng này đã bị xóa");
        if (!voucherStore.getCreatedBy().equals(username))
            throw new  BadRequestException( "Không có quyền truy cập");
        Query query = new Query();
        query.addCriteria(Criteria.where("voucher_store_id").is(voucherStoreId));
        if (Objects.nonNull(voucherCodeStatus))
            query.addCriteria(Criteria.where("voucher_code_status").is(voucherCodeStatus));
        Page<VoucherCode> voucherCodes = voucherCodeStorage.findAll(query, pageable);
        List<VoucherCodeResponse> voucherCodeResponses = voucherCodes.getContent()
                .stream()
                .map(VoucherCode::convertTovoucherCodeResponse)
                .collect(Collectors.toList());

        Page<VoucherCodeResponse> codeResponses = new PageImpl<>(voucherCodeResponses, pageable, voucherCodes.getTotalElements());
        return PageResponse.createFrom(codeResponses);
    }

    public List<UserVoucherResponse> getUserVoucher(String username, String productId) {
        List<UserVoucherResponse> responses = new ArrayList<>();
        Product product = productStorage.findProductById(productId);
        if(Objects.isNull(product)) throw new ResourceNotFoundException("Không tìm thấy sản phâm");
        List<VoucherStore> voucherStores = voucherStoreStorage.findByVoucherStoreTypeAndRefId(VoucherStoreType.PRODUCT, productId);
        voucherStores.addAll(voucherStoreStorage.findByVoucherStoreTypeAndRefId(VoucherStoreType.STORE, product.getSellerStoreId()));
        for(VoucherStore voucherStore : voucherStores){
            VoucherCode voucherCode = voucherCodeStorage.findFirstCodeCanUse(username, voucherStore.getId().toHexString());
            responses.add(new UserVoucherResponse(voucherStore.getVoucherStoreName(), voucherCode.getCode()));
        }
        return responses;
    }

}
