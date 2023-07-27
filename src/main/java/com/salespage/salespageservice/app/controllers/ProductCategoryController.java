package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.accountDtos.ShipperStatusDto;
import com.salespage.salespageservice.app.dtos.productDtos.CreateProductCategoryTypeDto;
import com.salespage.salespageservice.app.dtos.productDtos.UpdateProductCategoryTypeDto;
import com.salespage.salespageservice.domains.services.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/product-category")
@RestController
public class ProductCategoryController extends BaseController{
  @Autowired
  private ProductCategoryService productCategoryService;

  @GetMapping("")
  private ResponseEntity<?> getProductCategory(Authentication authentication){
    try{

      return successApi(productCategoryService.getProductCategory(getUsername(authentication)));
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @GetMapping("detaild")
  private ResponseEntity<?> getDetailProductCategory(Authentication authentication, @RequestParam String id){
    try{
      return successApi(productCategoryService.getDetailProductCategory(getUsername(authentication), id));
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }
  @PostMapping("")
  private ResponseEntity<?> createProductCategory(Authentication authentication, @RequestBody CreateProductCategoryTypeDto dto){
    try{
      productCategoryService.createProductCategory(getUsername(authentication), dto);
      return successApi("Tạo danh mục sản phẩm thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @PutMapping("")
  private ResponseEntity<?> updateProductCategory(Authentication authentication, @RequestBody UpdateProductCategoryTypeDto dto){
    try{
      productCategoryService.updateProductCategory(getUsername(authentication), dto);
      return successApi("Cập nhật danh mục sản phẩm thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @DeleteMapping("")
  private ResponseEntity<?> deleteProductCategory(Authentication authentication, @RequestParam String id){
    try{
      productCategoryService.deleteProductCategory(getUsername(authentication), id);
      return successApi("Xóa danh mục sản phẩm thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }
}
