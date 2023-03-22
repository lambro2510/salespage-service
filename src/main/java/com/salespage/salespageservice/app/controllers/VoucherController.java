package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.voucherDtos.VoucherStoreDto;
import com.salespage.salespageservice.domains.services.BaseService;
import com.salespage.salespageservice.domains.services.VoucherStoreService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("v1/api/voucher")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User", description = "The User API. Contains all the operations that can be performed on a user.")
public class VoucherController extends BaseController {
  @Autowired
  private VoucherStoreService voucherStoreService;
  public ResponseEntity<?> createVoucherStore(Authentication authentication, @RequestBody VoucherStoreDto voucherStoreDto){
    return voucherStoreService.createVoucherStore(getUsername(authentication), voucherStoreDto);
  }

  public ResponseEntity<?> updateVoucherStore(Authentication authentication, @RequestBody VoucherStoreDto voucherStoreDto, @RequestParam String voucherStoreId){
    return voucherStoreService.updateVoucherStore(getUsername(authentication), voucherStoreDto, voucherStoreId);
  }

  public ResponseEntity<?> deleteVoucherStore( Authentication authentication, @RequestParam String voucherStoreId){
    return voucherStoreService.deleteVoucherStore(getUsername(authentication), voucherStoreId);
  }

  public ResponseEntity<?> getAllVoucherStore(Authentication authentication){
    return voucherStoreService.getAllVoucherStore(getUsername(authentication));
  }
}
