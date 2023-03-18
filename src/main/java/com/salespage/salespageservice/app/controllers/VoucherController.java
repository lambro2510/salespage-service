package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.voucherDtos.VoucherStoreDto;
import com.salespage.salespageservice.domains.services.BaseService;
import com.salespage.salespageservice.domains.services.VoucherStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@CrossOrigin
@RequestMapping("v1/api/voucher")
public class VoucherController extends BaseController {
  @Autowired
  private VoucherStoreService voucherStoreService;
  public ResponseEntity<?> createVoucherStore(@ApiIgnore Authentication authentication, @RequestBody VoucherStoreDto voucherStoreDto){
    return voucherStoreService.createVoucherStore(getUsername(authentication), voucherStoreDto);
  }

  public ResponseEntity<?> updateVoucherStore(@ApiIgnore Authentication authentication, @RequestBody VoucherStoreDto voucherStoreDto, @RequestParam String voucherStoreId){
    return voucherStoreService.updateVoucherStore(getUsername(authentication), voucherStoreDto, voucherStoreId);
  }

  public ResponseEntity<?> deleteVoucherStore(@ApiIgnore Authentication authentication, @RequestParam String voucherStoreId){
    return voucherStoreService.deleteVoucherStore(getUsername(authentication), voucherStoreId);
  }

  public ResponseEntity<?> getAllVoucherStore(@ApiIgnore Authentication authentication){
    return voucherStoreService.getAllVoucherStore(getUsername(authentication));
  }
}
