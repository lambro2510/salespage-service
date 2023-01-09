package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionDto;
import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionInfoDto;
import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.services.ProductTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/product-transaction")
public class ProductTransactionController extends BaseController {

  @Autowired
  private ProductTransactionService productTransactionService;

  @PostMapping("")
  public ResponseEntity<ProductTransaction> createProductTransaction(Authentication authentication, @RequestBody ProductTransactionDto dto) {
    return productTransactionService.createProductTransaction(getUsername(authentication), dto);
  }

  @PutMapping("")
  public ResponseEntity<ProductTransaction> updateProductTransaction(Authentication authentication, @RequestBody ProductTransactionInfoDto dto) {
    return productTransactionService.updateProductTransaction(getUsername(authentication), dto);
  }

  @PutMapping("cancel")
  public ResponseEntity<ProductTransaction> cancelProductTransaction(Authentication authentication, @RequestParam String transactionId) {
    return productTransactionService.cancelProductTransaction(getUsername(authentication), transactionId);
  }

}
