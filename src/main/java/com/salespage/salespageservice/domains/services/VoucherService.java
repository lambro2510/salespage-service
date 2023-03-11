package com.salespage.salespageservice.domains.services;

import org.springframework.stereotype.Service;

@Service
public class VoucherService {
  public void createVoucherStore(){

  }

  public void updateVoucherStore(String voucherStoreId){

  }

  public void generateVoucherCode(String voucherStoreId, Long numberVoucherCode, Long expireTime){}

  public void useCode(String userId, String code){}

  public void deleteVoucherStore(String userId, String voucherStoreId){}


}
