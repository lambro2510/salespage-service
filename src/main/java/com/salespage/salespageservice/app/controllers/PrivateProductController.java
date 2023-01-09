package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.productDtos.ProductDto;
import com.salespage.salespageservice.app.dtos.productDtos.ProductInfoDto;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/product")
public class PrivateProductController extends BaseController {

  @Autowired
  private ProductService productService;

  @PostMapping("")
  public ResponseEntity<Product> createProduct(Authentication authentication, @RequestBody ProductInfoDto dto) {
    return productService.createProduct(getUsername(authentication), dto);
  }

  @PutMapping("")
  public ResponseEntity<Product> updateProduct(Authentication authentication, @RequestBody ProductDto dto) {
    return productService.updateProduct(getUsername(authentication), dto);
  }

}
