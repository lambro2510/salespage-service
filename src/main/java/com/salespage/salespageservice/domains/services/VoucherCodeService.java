package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.domains.entities.VoucherCode;
import com.salespage.salespageservice.domains.entities.VoucherStore;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class VoucherCodeService extends BaseService{

  @Autowired
  @Lazy
  private VoucherStoreService voucherStoreService;

  public void deleteAllVoucherCodeInStore(){

  }

  public void generateVoucherCode(String username, String voucherStoreId, Long numberVoucher, Date expireTime){
    voucherStoreService.updateQuantityOfVoucherStore(voucherStoreId, 0L, numberVoucher , username);
    for(int i = 0 ; i < numberVoucher; i++){
      VoucherCode voucherCode = new VoucherCode();
      voucherCode.setVoucherStoreId(voucherStoreId);
    }
  }
}
