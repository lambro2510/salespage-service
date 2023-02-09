package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.productDtos.ProductDto;
import com.salespage.salespageservice.app.dtos.productDtos.ProductInfoDto;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.utils.GoogleDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService extends BaseService {

  @Autowired
  private ProductTransactionService productTransactionService;

  @Autowired
  private GoogleDriver googleDriver;

  public ResponseEntity<Product> createProduct(String username, ProductInfoDto dto) {
    Product product = new Product();
    product.updateProduct(dto);
    product.setSellerUsername(username);

    productStorage.save(product);
    return ResponseEntity.ok(product);
  }

  public ResponseEntity<Product> updateProduct(String username, ProductDto dto) {
    Product product = productStorage.findProductById(dto.getProductId());
    if (Objects.isNull(product)) throw new ResourceNotFoundException("Product not exist");

    product.updateProduct(dto);
    return ResponseEntity.ok(product);
  }

  public ResponseEntity<PageResponse<Product>> getAllProduct(Pageable pageable) {
    Page<Product> productPage = productStorage.findAllProduct(pageable);
    return ResponseEntity.ok(PageResponse.createFrom(productPage));
  }

  public ResponseEntity<Product> getProductDetail(String productId) {
    return ResponseEntity.ok(productStorage.findProductById(productId));
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ResponseEntity<Boolean> deleteProduct(String username, String productId) {

    Product product = productStorage.findProductById(productId);

    if (!username.equals(product.getSellerUsername())) throw new ResourceNotFoundException("You haven't this item");

    productTransactionService.productTransactionCancel(productId);
    productStorage.delete(productId);
    return ResponseEntity.ok(true);
  }

  public ResponseEntity<List<String>> uploadProductImage(String username, String productId, List<MultipartFile> files) throws IOException {
    Product product = productStorage.findProductById(productId);
    if (!product.getSellerUsername().equals(username))
      throw new AuthorizationException("Can't upload image for this product");
    List<String> imageUrls = new ArrayList<>();
    for (MultipartFile multipartFile : files) {
      String imageUrl = googleDriver.uploadPublicImage(googleDriver.getFolderIdByName(productId), multipartFile.getName(), Helper.convertMultiPartToFile(multipartFile));
      product.getImageUrls().add(imageUrl);
      productStorage.save(product);
    }
    return ResponseEntity.ok(imageUrls);
  }

  public ResponseEntity<List<String>> deleteProductImages(String username, String productId, List<String> images) {
    Product product = productStorage.findProductById(productId);
    if (!product.getSellerUsername().equals(username))
      throw new AuthorizationException("Can't delete image for this product");
    List<String> imageUrls = new ArrayList<>();
    for (String imageUrl : images) {
      String fileId = Helper.extractFileIdFromUrl(imageUrl);
      googleDriver.deleteFile(fileId);
      imageUrls.add(fileId);
    }
    return ResponseEntity.ok(imageUrls);
  }
}