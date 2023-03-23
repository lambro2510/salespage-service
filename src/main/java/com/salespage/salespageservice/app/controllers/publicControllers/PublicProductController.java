package com.salespage.salespageservice.app.controllers.publicControllers;

import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("v1/api/public/product")
@Tag(name = "Quản lý sản phẩm", description = "API cho việc quản lý sản phẩm")
public class PublicProductController {
  @Autowired
  private ProductService productService;

  @GetMapping("")
  public ResponseEntity<PageResponse<Product>> getAllProduct(Pageable pageable) {
    return productService.getAllProduct(pageable);
  }

  @GetMapping("detail")
  public ResponseEntity<Product> getProductDetail(@RequestParam String productId) {
    return productService.getProductDetail(productId);
  }
}
