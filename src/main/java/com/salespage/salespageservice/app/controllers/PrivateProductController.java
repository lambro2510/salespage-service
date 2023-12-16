package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.domains.services.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product", description = "Thông tin sản phẩm được bán")
@CrossOrigin
@RestController
@RequestMapping("api/v1/product")
@SecurityRequirement(name = "bearerAuth")
public class PrivateProductController extends BaseController {

  @Autowired
  private ProductService productService;

}
