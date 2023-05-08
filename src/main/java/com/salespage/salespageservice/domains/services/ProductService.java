package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.productDtos.*;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductDataResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductTypeResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductType;
import com.salespage.salespageservice.domains.entities.ProductTypeDetail;
import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.entities.status.ProductTypeStatus;
import com.salespage.salespageservice.domains.entities.types.ResponseType;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.utils.Helper;
import jodd.util.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ProductService extends BaseService {

  @Autowired
  private ProductTransactionService productTransactionService;

  @Autowired
  private SellerStoreService sellerStoreService;


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

  public ResponseEntity<PageResponse<ProductResponse>> getAllProduct(String users, String productType, String productName, Long minPrice, Long maxPrice, String storeName, String username, Pageable pageable) {

    Query query = new Query();
    if (StringUtil.isNotBlank(username)) {
      query.addCriteria(Criteria.where("seller_username").ne(users));
    }
    if (StringUtil.isNotBlank(productName)) {
      Pattern pattern = Pattern.compile(".*" + productName + ".*", Pattern.CASE_INSENSITIVE);
      query.addCriteria(Criteria.where("product_name").regex(pattern));
    }

    if (StringUtil.isNotBlank(productType))
      query.addCriteria(Criteria.where("product_type").is(productType));
    if (minPrice != null)
      query.addCriteria(Criteria.where("price").gte(minPrice));
    if (maxPrice != null)
      query.addCriteria(Criteria.where("price").lte(maxPrice));
    if (StringUtil.isNotBlank(storeName)) {
      List<SellerStore> sellerStores = sellerStoreService.findIdsByStoreName(storeName);
      List<String> storeNames = sellerStores.stream()
              .map(SellerStore::getStoreName)
              .collect(Collectors.toList());
      query.addCriteria(Criteria.where("store_name").in(storeNames));
    }

    if (StringUtil.isNotBlank(username)) {
      List<SellerStore> sellerStores = sellerStoreService.findIdsByOwnerStoreName(username);
      List<String> ids = sellerStores.stream()
              .map(s -> s.getId().toHexString())
              .collect(Collectors.toList());
      query.addCriteria(Criteria.where("seller_store_id").in(ids));
    }
    Page<Product> productPage = productStorage.findAll(query, pageable);
    List<ProductResponse> products = productPage.getContent().stream().map(Product::assignToProductResponse).collect(Collectors.toList());
    for (ProductDataResponse response : products) {
      List<ProductTypeDetail> typeDetails = productTypeStorage.findByProductId(response.getProductId());
      response.setProductType(typeDetails.stream().map(ProductTypeDetail::getTypeDetailName).collect(Collectors.toList()));
    }
    return ResponseEntity.ok(PageResponse.createFrom(new PageImpl<>(products, pageable, productPage.getTotalElements())));
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

      for (MultipartFile multipartFile : multipartFiles) {
        String imageUrl = googleDriver.uploadPublicImageNotDelete("Product-" + productId, multipartFile.getName() + System.currentTimeMillis(), Helper.convertMultiPartToFile(multipartFile));
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

  public ResponseEntity<ResponseType> createProductType(String username, ProductTypeDto dto, List<UserRole> roles) {
    if (!hasUserRole(roles, UserRole.ADMIN) && !hasUserRole(roles, UserRole.OPERATOR))
      throw new AuthorizationException("Bạn không có quyền tạo mới");
    ProductType productType = new ProductType();
    productType.partnerFromDto(dto);
    productType.setCreatedBy(username);
    productType.setUpdatedBy(username);
    productTypeStorage.save(productType);
    return ResponseEntity.ok(ResponseType.CREATED);
  }

  public ResponseEntity<ResponseType> updateProductType(String username, ProductTypeDto dto, List<UserRole> roles) {
    if (!hasUserRole(roles, UserRole.ADMIN) && !hasUserRole(roles, UserRole.OPERATOR))
      throw new AuthorizationException("Bạn không có quyền tạo mới");
    ProductType productType = productTypeStorage.findByProductType(dto.getProductType());
    if (Objects.isNull(productType)) throw new ResourceNotFoundException("Không tồn tại loại sản phẩm này");
    productType.partnerFromDto(dto);
    productType.setCreatedBy(username);
    productType.setUpdatedBy(username);
    productTypeStorage.save(productType);
    return ResponseEntity.ok(ResponseType.UPDATED);
  }

  public ResponseEntity<ResponseType> createProductTypeDetail(ProductTypeDetailDto dto, String username) {
    ProductType productType = productTypeStorage.findByProductType(dto.getTypeName());
    if (Objects.isNull(productType)) throw new ResourceNotFoundException("Không tồn tại loại sản phẩm này");
    ProductTypeDetail productTypeDetail = new ProductTypeDetail();
    productTypeDetail.partnerFromDto(dto);
    productTypeDetail.setCreatedBy(username);
    productTypeDetail.setUpdatedBy(username);
    productTypeStorage.save(productTypeDetail);
    return ResponseEntity.ok(ResponseType.CREATED);
  }

  public ResponseEntity<ResponseType> updateProductTypeDetail(ProductTypeDetailDto dto, String productTypeId, String username) {
    ProductType productType = productTypeStorage.findByProductType(dto.getTypeName());
    if (Objects.isNull(productType)) throw new ResourceNotFoundException("Không tồn tại loại sản phẩm này");
    ProductTypeDetail typeDetail = productTypeStorage.findById(productTypeId);
    if (Objects.isNull(typeDetail)) throw new ResourceNotFoundException("Không tồn tại chi tiết loại sản phẩm này");
    if (!Objects.equals(typeDetail.getCreatedBy(), username))
      throw new AuthorizationException("Bạn không có quyền sửa");
    ProductTypeDetail productTypeDetail = new ProductTypeDetail();
    productTypeDetail.partnerFromDto(dto);
    productTypeDetail.setCreatedBy(username);
    productTypeDetail.setUpdatedBy(username);
    productTypeStorage.save(productTypeDetail);
    return ResponseEntity.ok(ResponseType.UPDATED);
  }

  public ResponseEntity<ResponseType> updateStatusTypeDetail(UpdateTypeDetailStatusDto dto, String username, List<UserRole> roles) {
    if (!hasUserRole(roles, UserRole.ADMIN) && !hasUserRole(roles, UserRole.OPERATOR))
      throw new AuthorizationException("Bạn không có quyền tạo mới");
    ProductTypeDetail productTypeDetail = productTypeStorage.findById(dto.getId());
    if (Objects.isNull(productTypeDetail))
      throw new ResourceNotFoundException("Không tồn tại chi tiết loại sản phẩm này");

    productTypeDetail.setStatus(dto.getStatus());
    productTypeDetail.setUpdatedBy(username);
    productTypeDetail.setUpdatedAt(System.currentTimeMillis());

    productTypeStorage.save(productTypeDetail);
    return ResponseEntity.ok(ResponseType.UPDATED);
  }

  public ResponseEntity<List<ProductType>> getAllProductType(List<UserRole> roles) {
    if (!hasUserRole(roles, UserRole.ADMIN) && !hasUserRole(roles, UserRole.OPERATOR))
      throw new AuthorizationException("Bạn không có quyền xem danh sách này");
    return ResponseEntity.ok(productTypeStorage.findAll());
  }

  public ResponseEntity<List<ProductTypeResponse>> getAllActiveProductType() {
    return ResponseEntity.ok(productTypeStorage.findByStatus(ProductTypeStatus.ACTIVE).stream().map(ProductType::partnerToProductTypeResponse).collect(Collectors.toList()));
  }
}