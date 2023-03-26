package com.salespage.salespageservice.app.controllers.publicControllers;

import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.types.ProductType;
import com.salespage.salespageservice.domains.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("v1/api/public/product")
@Tag(name = "Thông tin của sản phẩm", description = "Thông tin sản phẩm được bán")
public class PublicProductController {
  @Autowired
  private ProductService productService;

  @GetMapping("")
  public ResponseEntity<PageResponse<Product>> getAllProduct(@RequestParam(required = false) ProductType productType,
                                                             @RequestParam(required = false) String productName,
                                                             @RequestParam(required = false) Long minPrice,
                                                             @RequestParam(required = false) Long maxPrice,
                                                             @RequestParam(required = false) String storeName,
                                                             @RequestParam(required = false) String username,
                                                             Pageable pageable) {
    return productService.getAllProduct(productType,productName, minPrice, maxPrice, storeName, username, pageable);
  }

  @GetMapping("detail")
  public ResponseEntity<Product> getProductDetail(@RequestParam String productId) {
    return productService.getProductDetail(productId);
  }
}
