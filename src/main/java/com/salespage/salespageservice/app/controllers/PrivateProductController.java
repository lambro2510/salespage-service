package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.productDtos.ProductDto;
import com.salespage.salespageservice.app.dtos.productDtos.ProductInfoDto;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.services.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("v1/api/product")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User", description = "The User API. Contains all the operations that can be performed on a user.")
public class PrivateProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @PostMapping("")
    public ResponseEntity<Product> createProduct(Authentication authentication, @RequestBody ProductInfoDto dto) {
        return productService.createProduct(getUsername(authentication), dto);
    }

    @PostMapping("upload-images")
    public ResponseEntity<List<String>> uploadImages(Authentication authentication, @RequestParam String productId, @RequestParam List<MultipartFile> files) throws IOException {
        return productService.uploadProductImage(getUsername(authentication), productId, files);
    }

    @DeleteMapping("delete-images")
    public ResponseEntity<List<String>> deleteImages(Authentication authentication, @RequestParam String productId, @RequestBody List<String> imageUrls) throws IOException {
        return productService.deleteProductImages(getUsername(authentication), productId, imageUrls);
    }

    @DeleteMapping("")
    public ResponseEntity<Boolean> deleteProduct(Authentication authentication, @RequestParam String productId) throws IOException {
        return productService.deleteProduct(getUsername(authentication), productId);
    }

    @PutMapping("")
    public ResponseEntity<Product> updateProduct(Authentication authentication, @RequestBody ProductDto dto) {
        return productService.updateProduct(getUsername(authentication), dto);
    }

}
