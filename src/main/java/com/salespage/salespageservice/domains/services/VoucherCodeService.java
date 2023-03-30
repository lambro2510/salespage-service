package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.domains.entities.VoucherCode;
import com.salespage.salespageservice.domains.entities.VoucherCodeLimit;
import com.salespage.salespageservice.domains.entities.VoucherStore;
import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import com.salespage.salespageservice.domains.entities.status.VoucherStoreStatus;
import com.salespage.salespageservice.domains.entities.types.ResponseType;
import com.salespage.salespageservice.domains.entities.types.VoucherStoreType;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.exceptions.TransactionException;
import com.salespage.salespageservice.domains.exceptions.VoucherCodeException;
import com.salespage.salespageservice.domains.exceptions.info.ErrorCode;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class VoucherCodeService extends BaseService{

  @Autowired
  @Lazy
  private VoucherStoreService voucherStoreService;


  public void deleteAllVoucherCodeInStore(){

  }

  public ResponseEntity<?> generateVoucherCode(String username, String voucherStoreId, Long numberVoucher, Date expireTime){
    voucherStoreService.updateQuantityOfVoucherStore(voucherStoreId, 0L, numberVoucher , username);
    List<VoucherCode> voucherCodes = new ArrayList<>();
    for(int i = 0 ; i < numberVoucher; i++){
      VoucherCode voucherCode = new VoucherCode();
      voucherCode.setVoucherStoreId(voucherStoreId);
      voucherCode.setExpireTime(expireTime);
      voucherCode.setCode(UUID.randomUUID().toString());
    }
    voucherCodeStorage.saveAll(voucherCodes);
    return ResponseEntity.ok(ResponseType.CREATED);
  }

  @Transactional
  public ResponseEntity<?> receiveVoucher(String username, String voucherStoreId){
    VoucherStore voucherStore = voucherStoreStorage.findVoucherStoreById(voucherStoreId);

    if(Objects.isNull(voucherStore) || voucherStore.getVoucherStoreStatus().equals(VoucherStoreStatus.INACTIVE))
      throw new ResourceNotFoundException("Mã giảm giá hiện đã bị ngưng sử dụng");

    VoucherCodeLimit voucherCodeLimit = voucherCodeLimitStorage.findByUsernameAndVoucherStoreId(username, voucherStoreId);
    if(Objects.isNull(voucherCodeLimit)){
      voucherCodeLimit = new VoucherCodeLimit();
      voucherCodeLimit.setUsername(username);
      voucherCodeLimit.setVoucherStoreId(voucherStoreId);
      voucherCodeLimit.setNumberReceiveVoucher(0L);
    }
    voucherCodeLimit.setNumberReceiveVoucher(voucherCodeLimit.getNumberReceiveVoucher() + 1);
    if(voucherCodeLimit.getNumberReceiveVoucher() > voucherStore.getVoucherStoreDetail().getQuantityUsed())
      throw new VoucherCodeException(ErrorCode.VOUCHER_CODE, "Bạn đã nhận tối đa số lượng mã giảm giá");
    VoucherCode voucherCode = voucherCodeStorage.findFirstByVoucherStoreId(voucherStoreId, new Date());
    voucherCode.setOwnerId(username);
    voucherStore.getVoucherStoreDetail().setQuantityUsed(voucherStore.getVoucherStoreDetail().getQuantityUsed() + 1);
    voucherCodeStorage.save(voucherCode);
    voucherCodeLimitStorage.save(voucherCodeLimit);
    voucherStoreStorage.save(voucherStore);
    return ResponseEntity.ok(ResponseType.UPDATED);
  }
  public VoucherInfo useVoucher(String username, String code, String productId, Long productPrice) {
    VoucherInfo voucherInfo = new VoucherInfo();
    voucherInfo.setPriceBefore(new BigDecimal(productPrice));

    VoucherCode voucherCode = voucherCodeStorage.findByOwnerIdAndCode(username,code);
    if(Objects.isNull(voucherCode)) throw new ResourceNotFoundException("Mã giảm giá không hợp lệ");
    if(voucherCode.getExpireTime().after(new Date())) throw new TransactionException(ErrorCode.EXPIRE_VOUCHER,"Mã giảm giá đã hết hạn");

    VoucherStore voucherStore = voucherStoreStorage.findVoucherStoreById(voucherCode.getVoucherStoreId());

    if(Objects.isNull(voucherStore) || voucherStore.getVoucherStoreStatus().equals(VoucherStoreStatus.INACTIVE))
      throw new ResourceNotFoundException("Mã giảm giá hiện đã bị ngưng sử dụng");

    if(voucherStore.getVoucherStoreDetail().getMaxAblePrice() < productPrice || voucherStore.getVoucherStoreDetail().getMinAblePrice() > productPrice)
      throw new TransactionException(ErrorCode.TRANSACTION_EXCEPTION,"Mã không thể sử dụng cho sản phẩm này, không nằm trong giá trị mã có thể sử dụng");

    if(voucherStore.getVoucherStoreType() == VoucherStoreType.PRODUCT){
      if(!voucherStore.getProductId().equals(productId))
        throw new TransactionException(ErrorCode.TRANSACTION_EXCEPTION, "Mã giảm giá không áp dụng cho sản phẩm này");
      voucherInfo.setPriceAfter(BigDecimal.ZERO);
    }

    else if(voucherStore.getVoucherStoreType() == VoucherStoreType.DISCOUNT){
      long price = (productPrice - voucherStore.getValue());
      voucherInfo.setPriceAfter(new BigDecimal(price));
    }



    else if(voucherStore.getVoucherStoreType() == VoucherStoreType.DISCOUNT_PERCENT){
      long price = productPrice -  (productPrice * voucherStore.getValue())/100;
      if(price < 0) price = 0L;
      voucherInfo.setPriceAfter(new BigDecimal(price));
    }

    else throw new TransactionException(ErrorCode.TRANSACTION_EXCEPTION,"Mã không thể sử dụng cho sản phẩm này");

    voucherInfo.setVoucherCode(code);
    voucherInfo.setVoucherStoreType(voucherStore.getVoucherStoreType());
    voucherInfo.setValue(voucherStore.getValue());
    return voucherInfo;
  }
}
