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
import com.salespage.salespageservice.domains.utils.DateUtils;
import com.salespage.salespageservice.domains.utils.Helper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
  public void generateVoucherCode(String username, String voucherStoreId, Long numberVoucher, Long expireTime) {
    voucherStoreService.updateQuantityOfVoucherStore(voucherStoreId, numberVoucher, 0L, username);
    List<VoucherCode> voucherCodes = new ArrayList<>();
    for (int i = 0; i < numberVoucher; i++) {
      VoucherCode voucherCode = new VoucherCode();
      voucherCode.setVoucherStoreId(voucherStoreId);
      if (Objects.isNull(expireTime)) {
        voucherCode.setExpireTime(DateUtils.convertLocalDateTimeToLong(DateUtils.now().plusMonths(1)));
      } else {
        voucherCode.setExpireTime(expireTime);
      }
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
    VoucherCode voucherCode = voucherCodeStorage.findFirstVoucherCanUseByVoucherStoreId(voucherStoreId, DateUtils.now().toLocalDate());
    voucherCode.setUsername(username);
    voucherCode.setVoucherCodeStatus(VoucherCodeStatus.OWNER);
    voucherStore.getVoucherStoreDetail().setQuantityUsed(voucherStore.getVoucherStoreDetail().getQuantityUsed() + 1);
    voucherCodeStorage.save(voucherCode);
    voucherCodeLimitStorage.save(voucherCodeLimit);
    voucherStoreStorage.save(voucherStore);
    return voucherCode.getCode();
  }

  public VoucherInfo useVoucher(String username, String code, ProductTransaction productTransaction) {
    VoucherInfo voucherInfo = new VoucherInfo();
    voucherInfo.setPriceBefore(productTransaction.getTotalPrice());
    VoucherCode voucherCode = voucherCodeStorage.findCodeCanUse(username, code);
    if (Objects.isNull(voucherCode)) throw new ResourceNotFoundException("Mã giảm giá không hợp lệ");

    VoucherStore voucherStore = voucherStoreStorage.findVoucherStoreById(voucherCode.getVoucherStoreId());

    if (Objects.isNull(voucherStore) || !voucherStore.getVoucherStoreStatus().equals(VoucherStoreStatus.ACTIVE))
      throw new ResourceNotFoundException("Mã giảm giá hiện đã bị ngưng sử dụng");


    productTransaction.setTotalPrice(getPriceWhenUseVoucher(productTransaction.getTotalPrice(), voucherStore.getDiscountType(), voucherStore.getValue()));

    voucherCode.setUserAt(new Date());
    voucherCode.setVoucherCodeStatus(VoucherCodeStatus.USED);
    voucherCodeStorage.save(voucherCode);
    voucherInfo.setValue(voucherStore.getValue());
    voucherInfo.setVoucherStoreType(voucherStore.getVoucherStoreType());
    voucherInfo.setPriceAfter(productTransaction.getTotalPrice());
    voucherInfo.setTotalDiscount(voucherInfo.getPriceAfter() - voucherInfo.getPriceBefore());
    return voucherInfo;
  }

  public Double getPriceWhenUseVoucher(Double getTotalPrice, DiscountType type, Double value) {
    if (type.equals(DiscountType.PERCENT)) {
      return getTotalPrice * (value / 100);
    } else {
      return getTotalPrice * value;
    }
  }

  public PageResponse<VoucherCodeResponse> getAllVoucherCodeInStore(String username, String voucherStoreId, VoucherCodeStatus voucherCodeStatus, Pageable pageable) {
    VoucherStore voucherStore = voucherStoreStorage.findVoucherStoreById(voucherStoreId);
    if (Objects.isNull(voucherStore))
      throw new BadRequestException("Cửa hàng này đã bị xóa");
    if (!voucherStore.getCreatedBy().equals(username))
      throw new BadRequestException("Không có quyền truy cập");
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
    if (Objects.isNull(product)) throw new ResourceNotFoundException("Không tìm thấy sản phâm");
    List<VoucherStore> voucherStores = voucherStoreStorage.findByVoucherStoreTypeAndRefId(VoucherStoreType.PRODUCT, productId);
    for (String storeId : product.getSellerStoreIds()) {
      voucherStores.addAll(voucherStoreStorage.findByVoucherStoreTypeAndRefId(VoucherStoreType.STORE, storeId));
    }
    for (VoucherStore voucherStore : voucherStores) {
      VoucherCode voucherCode = voucherCodeStorage.findFirstCodeCanUse(username, voucherStore.getId().toHexString());
      if (Objects.isNull(voucherCode)) continue;
      responses.add(UserVoucherResponse
          .builder()
          .voucherCodeId(voucherCode.getId().toHexString())
          .voucherStoreName(voucherStore.getVoucherStoreName())
          .voucherCode(voucherCode.getCode())
          .discountType(voucherStore.getDiscountType())
          .storeType(voucherStore.getVoucherStoreType())
          .dayToExpireTime(voucherCode.getExpireTime())
          .minPrice(voucherStore.getVoucherStoreDetail().getMinAblePrice())
          .maxPrice(voucherStore.getVoucherStoreDetail().getMaxAblePrice())
          .value(voucherStore.getValue())
          .build());
    }
    return responses;
  }

  public VoucherInfo getVoucherInfo(String voucherCodeId, String username, Product product, Double sellPrice, boolean isThrowErr) {
    try {
      if (StringUtils.isBlank(voucherCodeId)) {
        return null;
      }

      VoucherCode voucherCode = voucherCodeStorage.findById(voucherCodeId);
      if (voucherCode == null) {
        throw new ResourceNotFoundException("Mã không tồn tại");
      }
      if (voucherCode.checkVoucher(username)) {
        throw new BadRequestException("Mã không hợp lệ");
      }
      VoucherStore voucherStore = voucherStoreStorage.findVoucherStoreById(voucherCode.getVoucherStoreId());
      if (voucherStore == null) {
        throw new ResourceNotFoundException("Cửa hàng không tồn tại");
      }
      if (voucherStore.getVoucherStoreType().equals(VoucherStoreType.PRODUCT)) {
        if (!product.getId().toHexString().equals(voucherStore.getRefId())) {
          throw new BadRequestException("Mã không hợp lệ");
        }
      } else {
        if (!product.getSellerStoreIds().contains(voucherStore.getRefId())) {
          throw new BadRequestException("Mã không hợp lệ");
        }
      }
      return new VoucherInfo(voucherCode, voucherStore, sellPrice);
    } catch (Exception ex) {
      if (isThrowErr) {
        throw new BadRequestException(ex.getMessage());
      }
    }
    return null;
  }

  public VoucherInfo getVoucherInfo(VoucherCode voucherCode, VoucherStore voucherStore, String username, Product product, Double sellPrice, boolean isThrowErr) {
    try {
      if (voucherCode == null) {
        throw new ResourceNotFoundException("Mã không tồn tại");
      }
      if (voucherCode.checkVoucher(username)) {
        throw new BadRequestException("Mã không hợp lệ");
      }
      if (voucherStore == null) {
        throw new ResourceNotFoundException("Cửa hàng không tồn tại");
      }
      if (voucherStore.getVoucherStoreType().equals(VoucherStoreType.PRODUCT)) {
        if (!product.getId().toHexString().equals(voucherStore.getRefId())) {
          throw new BadRequestException("Mã không hợp lệ");
        }
      } else {
        if (!product.getSellerStoreIds().contains(voucherStore.getRefId())) {
          throw new BadRequestException("Mã không hợp lệ");
        }
      }
      return new VoucherInfo(voucherCode, voucherStore, sellPrice);
    } catch (Exception ex) {
      if (isThrowErr) {
        throw new BadRequestException(ex.getMessage());
      }
    }
    return null;
  }

  public VoucherInfo getVoucherInfoAndUse(String voucherCodeId, String username, Product product, Double sellPrice) {
    if (StringUtils.isBlank(voucherCodeId)) {
      return null;
    }

    VoucherCode voucherCode = voucherCodeStorage.findById(voucherCodeId);
    if (voucherCode == null) {
      throw new ResourceNotFoundException("Mã không tồn tại");
    }
    if (voucherCode.checkVoucher(username)) {
      throw new BadRequestException("Mã không hợp lệ");
    }
    VoucherStore voucherStore = voucherStoreStorage.findVoucherStoreById(voucherCode.getVoucherStoreId());
    if (voucherStore == null) {
      throw new ResourceNotFoundException("Cửa hàng không tồn tại");
    }

    if (voucherStore.getVoucherStoreStatus() != VoucherStoreStatus.ACTIVE) {
      throw new BadRequestException("Mã giảm giá đạng không được sử dụng");
    }

    if (voucherStore.getVoucherStoreDetail().getMaxAblePrice() < sellPrice || voucherStore.getVoucherStoreDetail().getMinAblePrice() > sellPrice) {
      throw new BadRequestException("Voucher chỉ có thể sử dụng cho các sản phẩm có giá trị từ " + voucherStore.getVoucherStoreDetail().getMinAblePrice() + "đến " + voucherStore.getVoucherStoreDetail().getMaxAblePrice());
    }

    if (voucherStore.getVoucherStoreType().equals(VoucherStoreType.PRODUCT)) {
      if (!product.getId().toHexString().equals(voucherStore.getRefId())) {
        throw new BadRequestException("Mã không hợp lệ");
      }
    } else {
      if (!product.getSellerStoreIds().contains(voucherStore.getRefId())) {
        throw new BadRequestException("Mã không hợp lệ");
      }
    }


    return new VoucherInfo(voucherCode, voucherStore, sellPrice);
  }


}
