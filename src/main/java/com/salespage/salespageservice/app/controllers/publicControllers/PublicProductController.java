package com.salespage.salespageservice.app.controllers.publicControllers;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductDetailResponse;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.services.ProductService;
import com.salespage.salespageservice.domains.services.SearchHistoryService;
import com.salespage.salespageservice.domains.services.StatisticService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("api/v1/public/product")
@Tag(name = "Product", description = "Thông tin sản phẩm được bán")
@Log4j2
public class PublicProductController extends BaseController {
  @Autowired
  private ProductService productService;

  @Autowired
  private StatisticService statisticService;

  @Autowired
  private SearchHistoryService searchHistoryService;
  @GetMapping("")
  public ResponseEntity<BaseResponse> getAllProduct(
      @RequestParam(required = false) String productId,
      @RequestParam(required = false) String productName,
      @RequestParam(required = false) Long minPrice,
      @RequestParam(required = false) Long maxPrice,
      @RequestParam(required = false) String storeName,
      @RequestParam(required = false) String ownerStoreUsername,
      @RequestParam(required = false) Long lte,
      @RequestParam(required = false) Long gte,
      Authentication authentication,
      Pageable pageable) {
    try {
      if (Objects.nonNull(authentication)) {
        String username = getUsername(authentication);
        searchHistoryService.updateSearchHistory(username, productName, storeName, ownerStoreUsername);
        log.info("getProductDetail with username: {{}}", username);
      }
      return successApi(productService.findProduct(productId, productName, minPrice, maxPrice, storeName, ownerStoreUsername, lte, gte, pageable));

    } catch (Exception ex) {
      log.error("=========>getAllProduct: ", ex);
      return errorApi(ex.getMessage());
    }
  }

  @GetMapping("hot-product")
  public ResponseEntity<BaseResponse> getHotProduct(
      Authentication authentication) {
    try {
      String username = null;
      if (Objects.nonNull(authentication)) {
        username = getUsername(authentication);
        log.info("getProductDetail with username: {{}}", username);
      }
      return successApi(productService.findHotProduct(username));

    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @GetMapping("suggest/{productId}")
  public ResponseEntity<BaseResponse> getSuggestProduct(@PathVariable String productId) {
    try {
      return successApi(productService.getSuggestProduct(productId));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @GetMapping("{productId}")
  public ResponseEntity<BaseResponse> getProductDetail(Authentication authentication, @PathVariable String productId) {
    try {
      String username = null;
      ProductDetailResponse response = new ProductDetailResponse();
      if (Objects.nonNull(authentication)) {
        username = getUsername(authentication);
        log.info("getProductDetail with username: {{}}", username);
      }
      statisticService.updateView(productId);
      response = productService.getProductDetail(username, productId);
      productService.getRating(username, response);
      return successApi(response);
    } catch (Exception ex) {
      log.error(ex);
      return errorApi(ex.getMessage());
    }
  }

  @GetMapping("type")
  public ResponseEntity<BaseResponse> getAllActiveProductType(@RequestParam(required = false) String productTypeName) {
    return successApi(productService.getAllActiveProductType(productTypeName));
  }
}
