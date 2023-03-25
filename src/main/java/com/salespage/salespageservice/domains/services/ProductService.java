package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.productDtos.ProductDto;
import com.salespage.salespageservice.app.dtos.productDtos.ProductInfoDto;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.entities.types.ProductType;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.utils.GoogleDriver;
import com.salespage.salespageservice.domains.utils.Helper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ProductService extends BaseService {

  @Autowired
  private ProductTransactionService productTransactionService;

  @Autowired
  private SellerStoreService sellerStoreService;
  @Autowired
  private GoogleDriver googleDriver;

  public ResponseEntity<Product> createProduct(String username, ProductInfoDto dto) {
    SellerStore sellerStore = sellerStoreStorage.findById(dto.getStoreId());
    if (!Objects.equals(sellerStore.getOwnerStoreName(), username)) {
      throw new AuthorizationException("Không được phép");
    }
    Product product = new Product();
    product.updateProduct(dto);
    product.setSellerUsername(username);

    productStorage.save(product);
    return ResponseEntity.ok(product);
  }

  public ResponseEntity<Product> updateProduct(String username, ProductDto dto) {
    Product product = productStorage.findProductById(dto.getProductId());
    if (Objects.isNull(product)) throw new ResourceNotFoundException("Không tòn tại sản phẩm này hoặc đã bị xóa");

    product.updateProduct(dto);
    return ResponseEntity.ok(product);
  }

  public ResponseEntity<PageResponse<Product>> getAllProduct(ProductType productType, Long minPrice, Long maxPrice, String storeName, String username, Pageable pageable) {

    Query query = new Query();
    if(productType != null)
      query.addCriteria(Criteria.where("product_type").is(productType));
    if(minPrice != null)
      query.addCriteria(Criteria.where("price").gte(minPrice));
    if(productType != null)
      query.addCriteria(Criteria.where("price").lte(maxPrice));
    if (storeName != null) {
      List<SellerStore> sellerStores = sellerStoreService.findIdsByStoreName(storeName);
      List<String> ids = sellerStores.stream()
          .map(s -> s.getId().toHexString())
          .collect(Collectors.toList());
      query.addCriteria(Criteria.where("seller_store_id").in(ids));
    }
    if (username != null) {
      List<SellerStore> sellerStores = sellerStoreService.findIdsByOwnerStoreName(username);
      List<String> ids = sellerStores.stream()
          .map(s -> s.getId().toHexString())
          .collect(Collectors.toList());
      query.addCriteria(Criteria.where("seller_store_id").in(ids));
    }
    Page<Product> productPage = productStorage.findAll(query,pageable);
    return ResponseEntity.ok(PageResponse.createFrom(productPage));
  }

  public ResponseEntity<Product> getProductDetail(String productId) {
    return ResponseEntity.ok(productStorage.findProductById(productId));
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ResponseEntity<Boolean> deleteProduct(String username, String productId) throws IOException {

    Product product = productStorage.findProductById(productId);

    if (!username.equals(product.getSellerUsername())) throw new ResourceNotFoundException("You haven't this item");

    productTransactionService.productTransactionCancel(productId);
    productStorage.delete(productId);
    googleDriver.deleteFolderByName(productId);
    return ResponseEntity.ok(true);
  }

  public ResponseEntity<List<String>> uploadProductImage(String username, String productId, List<MultipartFile> multipartFiles) throws IOException {
    List<String> imageUrls = new ArrayList<>();
    try {
      Product product = productStorage.findProductById(productId);
      if (product == null) throw new ResourceNotFoundException("Không tòn tại sản phẩm này hoặc đã bị xóa");
      if (!product.getSellerUsername().equals(username))
        throw new AuthorizationException("Không được phép");

      for(MultipartFile multipartFile : multipartFiles){
        String imageUrl = googleDriver.uploadPublicImageNotDelete(googleDriver.getFolderIdByName("Product-" + productId), multipartFile.getName() + System.currentTimeMillis(), Helper.convertMultiPartToFile(multipartFile));
        product.getImageUrls().add(imageUrl);
        imageUrls.add(imageUrl);
        productStorage.save(product);
      }

    } catch (Exception ex) {
      log.error(ex.getMessage());
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