package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.domains.services.SellerStoreService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("api/v1/seller-store")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Store", description = "Quản lý cửa hàng của người dùng")
public class StoreController extends BaseController {

  @Autowired
  private SellerStoreService sellerStoreService;


}
