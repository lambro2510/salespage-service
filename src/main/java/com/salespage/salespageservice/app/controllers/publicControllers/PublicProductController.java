package com.salespage.salespageservice.app.controllers.publicControllers;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductDataResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductTypeResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("v1/api/public/product")
@Tag(name = "Thông tin của sản phẩm", description = "Thông tin sản phẩm được bán")
public class PublicProductController extends BaseController {
  @Autowired
  private ProductService productService;

  @GetMapping("")
  public ResponseEntity<PageResponse<ProductDataResponse>> getAllProduct(@RequestParam(required = false) String productType,
                                                                         @RequestParam(required = false) String productName,
                                                                         @RequestParam(required = false) Long minPrice,
                                                                         @RequestParam(required = false) Long maxPrice,
                                                                         @RequestParam(required = false) String storeName,
                                                                         @RequestParam(required = false) String ownerStoreUsername,
                                                                         Authentication authentication,
                                                                         Pageable pageable) {
    return productService.getAllProduct(getUsername(authentication), productType, productName, minPrice, maxPrice, storeName, ownerStoreUsername, pageable);
  }

  @GetMapping("detail")
  public ResponseEntity<Product> getProductDetail(@RequestParam String productId) {
    return productService.getProductDetail(productId);
  }

  @GetMapping("type")
  public ResponseEntity<List<ProductTypeResponse>> getAllActiveProductType() {
    return productService.getAllActiveProductType();
  }
}
