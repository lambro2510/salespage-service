package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.productDtos.*;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductDetailResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductItemResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductTypeResponse;
import com.salespage.salespageservice.app.responses.UploadImageData;
import com.salespage.salespageservice.domains.entities.*;
import com.salespage.salespageservice.domains.entities.infor.Rate;
import com.salespage.salespageservice.domains.entities.status.ProductTypeStatus;
import com.salespage.salespageservice.domains.entities.types.FavoriteType;
import com.salespage.salespageservice.domains.entities.types.RatingType;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.utils.Helper;
import jodd.util.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ProductService extends BaseService {

  @Autowired
  private ProductTransactionService productTransactionService;

  @Autowired
  private SellerStoreService sellerStoreService;


  public Product createProduct(String username, CreateProductInfoDto dto) {
    SellerStore sellerStore = sellerStoreStorage.findById(dto.getStoreId());
    if (Objects.isNull(sellerStore)) throw new ResourceNotFoundException("Không tồn tại cửa hàng này");
    if (!Objects.equals(sellerStore.getOwnerStoreName(), username)) {
      throw new AuthorizationException("Không được phép");
    }
    ProductCategory productCategory = productCategoryStorage.findByCreatedByAndId(username, dto.getCategoryId());
    if (Objects.isNull(productCategory)) throw new ResourceNotFoundException("Không tồn tại danh mục này");

    Product product = new Product();
    product.updateProduct(dto);
    product.setSellerUsername(username);
    product.setSellerStoreId(sellerStore.getId().toHexString());
    productStorage.save(product);
    return product;
  }

  public Product updateProduct(String username,String productId,  CreateProductInfoDto dto) {
    Product product = productStorage.findProductById(productId);
    if (Objects.isNull(product)) throw new ResourceNotFoundException("Không tồn tại sản phẩm này hoặc đã bị xóa");
    if (!Objects.equals(product.getSellerUsername(), username))
      throw new AuthorizationException("Bạn không có quyền cập nhật sản phẩm này");

    ProductCategory productCategory = productCategoryStorage.findByCreatedByAndId(username, dto.getCategoryId());
    if (Objects.isNull(productCategory)) throw new ResourceNotFoundException("Không tồn tại danh mục này");

    SellerStore sellerStore = sellerStoreStorage.findById(dto.getStoreId());
    if (Objects.isNull(sellerStore)) throw new ResourceNotFoundException("Không tồn tại danh mục này");

    product.updateProduct(dto);
    productStorage.save(product);
    return product;
  }

  public Page<Product> getAllProduct(String username, String productId, String productName, Long minPrice, Long maxPrice, String storeName, String sellerStoreUsername, Long lte, Long gte, Pageable pageable) {
    Query query = new Query();
    if (StringUtil.isNotBlank(username)) {
      query.addCriteria(Criteria.where("seller_username").is(username));
    }
    if (StringUtil.isNotBlank(productId) && ObjectId.isValid(productId)) {
      query.addCriteria(Criteria.where("_id").is(new ObjectId(productId)));
    }
    if (StringUtil.isNotBlank(productName)) {
      Pattern pattern = Pattern.compile(".*" + productName + ".*", Pattern.CASE_INSENSITIVE);
      query.addCriteria(Criteria.where("product_name").regex(pattern));
    }
    if (minPrice != null)
      query.addCriteria(Criteria.where("price").gte(minPrice));
    if (maxPrice != null)
      query.addCriteria(Criteria.where("price").lte(maxPrice));
    if (Objects.nonNull(lte) && Objects.nonNull(gte)) {
      query.addCriteria(Criteria.where("created_at").lte(gte).andOperator(Criteria.where("created_at").gte(lte)));
    }
    if (StringUtil.isNotBlank(storeName)) {
      List<SellerStore> sellerStores = sellerStoreService.findIdsByStoreName(storeName);
      List<String> storeNames = sellerStores.stream()
          .map(SellerStore::getStoreName)
          .collect(Collectors.toList());
      query.addCriteria(Criteria.where("store_name").in(storeNames));
    }

    if (StringUtil.isNotBlank(sellerStoreUsername)) {
      List<SellerStore> sellerStores = sellerStoreService.findIdsByOwnerStoreName(sellerStoreUsername);
      List<String> ids = sellerStores.stream()
          .map(s -> s.getId().toHexString())
          .collect(Collectors.toList());
      query.addCriteria(Criteria.where("seller_store_id").in(ids));
    }

    return productStorage.findAll(query, pageable);
  }

  public PageResponse<ProductItemResponse> getAllProduct(String productId, String storeName, Pageable pageable){
    Query query = new Query();
    if (StringUtil.isNotBlank(productId) && ObjectId.isValid(productId)) {
      query.addCriteria(Criteria.where("_id").is(new ObjectId(productId)));
    }
    if (StringUtil.isNotBlank(storeName)) {
      List<SellerStore> sellerStores = sellerStoreService.findIdsByStoreName(storeName);
      List<String> storeNames = sellerStores.stream()
          .map(SellerStore::getStoreName)
          .collect(Collectors.toList());
      query.addCriteria(Criteria.where("store_name").in(storeNames));
    }

    Page<Product> products = productStorage.findAll(query, pageable);
    List<ProductItemResponse> responses = products.getContent().stream().map(Product::assignToProductItemResponse).collect(Collectors.toList());
    Map<String, SellerStore> sellerStoreMap = new HashMap<>();
    Map<String, ProductCategory> productCategoryMap = new HashMap<>();
    for(ProductItemResponse itemResponses : responses){
      SellerStore sellerStore = sellerStoreMap.get(itemResponses.getStoreId());
      if(Objects.isNull(sellerStore)){
        sellerStore = sellerStoreStorage.findById(itemResponses.getStoreId());
        sellerStoreMap.put(itemResponses.getStoreId(), sellerStore);
      }

      ProductCategory productCategory = productCategoryMap.get(itemResponses.getCategoryId());
      if(Objects.isNull(productCategory)){
        productCategory = productCategoryStorage.findById(itemResponses.getCategoryId());
        productCategoryMap.put(itemResponses.getCategoryId(), productCategory);
      }

      itemResponses.setCategoryName(productCategory.getCategoryName());
      itemResponses.setStoreName(sellerStore.getStoreName());
    }
    Page<ProductItemResponse> itemResponses = new PageImpl<>(responses, pageable, products.getTotalElements());
    return PageResponse.createFrom(itemResponses);
  }

  public PageResponse<ProductItemResponse> findProduct(String productId, String productName, Long minPrice, Long maxPrice, String storeName, String username, Long lte, Long gte, Pageable pageable) {
    Page<Product> productPage = getAllProduct(null, productId, productName, minPrice, maxPrice, storeName, username, lte, gte, pageable);

    List<ProductItemResponse> products = productPage.getContent().stream().map(Product::assignToProductItemResponse).collect(Collectors.toList());
    Map<String, List<Product>> productsByStoreId = productPage.getContent().stream()
        .collect(Collectors.groupingBy(Product::getSellerStoreId));
    List<String> listStoreId = new ArrayList<>(productsByStoreId.keySet());
    List<SellerStore> sellerStores = sellerStoreStorage.findByIdIn(Helper.convertListStringToListObjectId(listStoreId));
    Map<ObjectId, SellerStore> sellerStoreMap = sellerStores.stream().collect(Collectors.toMap(SellerStore::getId, Function.identity()));
    for (ProductItemResponse response : products) {
      ProductCategory productCategory = productCategoryStorage.findById(response.getCategoryId());
      if (Objects.isNull(productCategory)) throw new ResourceNotFoundException("Không tìm thấy danh mục sản phẩm");
      response.setCategoryName(productCategory.getCategoryName());

      SellerStore store = sellerStoreMap.get(new ObjectId(response.getStoreId()));
      if (Objects.nonNull(store)) {
        response.setStoreName(store.getStoreName());
      }
      List<ProductTypeDetail> typeDetails = productTypeStorage.findByProductId(response.getProductId());
      response.setProductTypes(typeDetails.stream().map(ProductTypeDetail::getTypeDetailName).collect(Collectors.toList()));
    }


    return PageResponse.createFrom(new PageImpl<>(products, pageable, productPage.getTotalElements()));
  }

  public ProductDetailResponse getSellerProductDetail(String productId) throws Exception {
    ProductDetailResponse response;
    Product product = productStorage.findProductById(productId);
    response = product.assignToProductDetailResponse();
    SellerStore sellerStore = sellerStoreStorage.findById(product.getSellerStoreId());

    response.assignFromStore(sellerStore);

    ProductCategory productCategory = productCategoryStorage.findById(product.getCategoryId());
    if (Objects.isNull(productCategory)) throw new ResourceNotFoundException("Không tìm thấy danh mục sản phẩm");
    response.assignFromCategory(productCategory);
    return response;
  }

  public ProductDetailResponse getProductDetail(String username, String productId) throws Exception {
    ProductDetailResponse response = new ProductDetailResponse();
    Product product = productStorage.findProductById(productId);
    response = product.assignToProductDetailResponse();
    SellerStore sellerStore = sellerStoreStorage.findById(product.getSellerStoreId());

    if (Objects.nonNull(username)) {
      UserFavorite userFavorite = userFavoriteStorage.findByUsernameAndRefIdAndFavoriteType(username, productId, FavoriteType.PRODUCT);
      Rating rating = ratingStorage.findByUsernameAndRefIdAndAndRatingType(username, productId, RatingType.PRODUCT);
      if (Objects.isNull(rating)) rating = new Rating();
      response.setIsLike(!Objects.isNull(userFavorite) && userFavorite.getLike());
      response.setRate(rating.getPoint());
    }

    //assign from store
    response.assignFromStore(sellerStore);

    //assign from favorite


    //assign from category
    ProductCategory productCategory = productCategoryStorage.findById(product.getCategoryId());
    if (Objects.isNull(productCategory)) throw new ResourceNotFoundException("Không tìm thấy danh mục sản phẩm");
    response.assignFromCategory(productCategory);

    List<Product> similarProducts = findSimilarProducts(product, productCategory.getCategoryName());
    List<ProductResponse> listSimilarProduct = similarProducts.stream().map(Product::assignToProductResponse).collect(Collectors.toList());
    response.setSimilarProducts(listSimilarProduct);
    return response;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Boolean deleteProduct(String username, String productId) throws IOException {

    Product product = productStorage.findProductById(productId);

    if (!username.equals(product.getSellerUsername()))
      throw new ResourceNotFoundException("Bạn không có sản phẩm này");

    productTransactionService.productTransactionCancel(productId);
    productStorage.delete(productId);
    googleDriver.deleteFolderByName(productId);
    return true;
  }

  public UploadImageData uploadProductImage(String username, String productId, MultipartFile file) throws IOException {
    List<UploadImageData> imageUrls = new ArrayList<>();
    Product product = productStorage.findProductById(productId);
    if (product == null) throw new ResourceNotFoundException("Không tòn tại sản phẩm này hoặc đã bị xóa");
    if (!product.getSellerUsername().equals(username))
      throw new AuthorizationException("Không được phép");

    String imageUrl = googleDriver.uploadPublicImageNotDelete("Product-" + productId, file.getName() + System.currentTimeMillis(), Helper.convertMultiPartToFile(file));
    product.getImageUrls().add(imageUrl);
    imageUrls.add(new UploadImageData(Helper.generateRandomString(), Helper.generateRandomString() + ".png", "done", imageUrl, imageUrl));

    product.setDefaultImageUrl(imageUrl);
    productStorage.save(product);
    return imageUrls.get(0);
  }

  public void updateDefaultImage(String username, String productId, String imageUrl) {
    Product product = productStorage.findProductById(productId);
    if (product == null) throw new ResourceNotFoundException("Không tòn tại sản phẩm này hoặc đã bị xóa");
    if (!product.getSellerUsername().equals(username))
      throw new AuthorizationException("Không được phép");

    product.setDefaultImageUrl(imageUrl);
    productStorage.save(product);
  }

  public List<String> deleteProductImages(String username, String productId, String images) {
    Product product = productStorage.findProductById(productId);
    if (!product.getSellerUsername().equals(username))
      throw new AuthorizationException("Bạn không thể xóa ảnh của sản phẩm này");
    String[] listImages = images.split(",");
    List<String> imageUrls = new ArrayList<>();
    for (String imageUrl : listImages) {
      String fileId = Helper.extractFileIdFromUrl(imageUrl);
      googleDriver.deleteFile(fileId);
      imageUrls.add(fileId);
      product.getImageUrls().remove(imageUrl);
    }
    productStorage.save(product);
    return imageUrls;
  }

  public void createProductType(String username, ProductTypeDto dto, List<UserRole> roles) {
    if (!hasUserRole(roles, UserRole.ADMIN) && !hasUserRole(roles, UserRole.OPERATOR))
      throw new AuthorizationException("Bạn không có quyền tạo mới");
    ProductType productType = new ProductType();
    productType.partnerFromDto(dto);
    productType.setCreatedBy(username);
    productType.setUpdatedBy(username);
    productTypeStorage.save(productType);
  }

  public void updateProductType(String username, ProductTypeDto dto, List<UserRole> roles) {
    if (!hasUserRole(roles, UserRole.ADMIN) && !hasUserRole(roles, UserRole.OPERATOR))
      throw new AuthorizationException("Bạn không có quyền tạo mới");
    ProductType productType = productTypeStorage.findByProductType(dto.getProductType());
    if (Objects.isNull(productType)) throw new ResourceNotFoundException("Không tồn tại loại sản phẩm này");
    productType.partnerFromDto(dto);
    productType.setCreatedBy(username);
    productType.setUpdatedBy(username);
    productTypeStorage.save(productType);
  }

  public void createProductTypeDetail(ProductTypeDetailDto dto, String username) {
    ProductType productType = productTypeStorage.findByProductType(dto.getTypeName());
    if (Objects.isNull(productType)) throw new ResourceNotFoundException("Không tồn tại loại sản phẩm này");
    ProductTypeDetail productTypeDetail = new ProductTypeDetail();
    productTypeDetail.partnerFromDto(dto);
    productTypeDetail.setCreatedBy(username);
    productTypeDetail.setUpdatedBy(username);
    productTypeStorage.save(productTypeDetail);
  }

  public void updateProductTypeDetail(ProductTypeDetailDto dto, String productTypeId, String username) {
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
  }

  public void updateStatusTypeDetail(UpdateTypeDetailStatusDto dto, String username, List<UserRole> roles) {
    if (!hasUserRole(roles, UserRole.ADMIN) && !hasUserRole(roles, UserRole.OPERATOR))
      throw new AuthorizationException("Bạn không có quyền tạo mới");
    ProductTypeDetail productTypeDetail = productTypeStorage.findById(dto.getId());
    if (Objects.isNull(productTypeDetail))
      throw new ResourceNotFoundException("Không tồn tại chi tiết loại sản phẩm này");

    productTypeDetail.setStatus(dto.getStatus());
    productTypeDetail.setUpdatedBy(username);
    productTypeDetail.setUpdatedAt(System.currentTimeMillis());

    productTypeStorage.save(productTypeDetail);
  }

  public List<ProductType> getAllProductType(List<UserRole> roles) {
    if (!hasUserRole(roles, UserRole.ADMIN) && !hasUserRole(roles, UserRole.OPERATOR))
      throw new AuthorizationException("Bạn không có quyền xem danh sách này");
    return productTypeStorage.findAll();
  }

  public List<ProductTypeResponse> getAllActiveProductType() {
    return productTypeStorage.findByStatus(ProductTypeStatus.ACTIVE).stream().map(ProductType::partnerToProductTypeResponse).collect(Collectors.toList());
  }

  private List<Product> findSimilarProducts(Product product, String categoryName) throws Exception {
    List<ProductCategory> productCategories = productCategoryStorage.findByCategoryName(categoryName);

    List<String> categoriesId = productCategories.stream().map(item -> item.getId().toHexString()).collect(Collectors.toList());
    List<Product> products = productStorage.findTop11ByCategoryIdIn(categoriesId);

    products = products.stream().filter(item -> !item.getId().equals(product.getId())).collect(Collectors.toList());
    return products;
  }

  @Transactional
  public Rate updateRating(String username, String productId, Float point) {
    User user = userStorage.findByUsername(username);
    if (Objects.isNull(user)) throw new ResourceNotFoundException("Không tồn tại người dùng này");

    Product product = productStorage.findProductById(productId);
    if (Objects.isNull(product)) throw new ResourceNotFoundException("Không tồn tại sản phẩm này");

    Rating rating = ratingStorage.findByUsernameAndRefIdAndAndRatingType(username, productId, RatingType.PRODUCT);
    Rate rate = product.getRate();
    if (Objects.isNull(rating)) {
      rating = new Rating(new ObjectId(), username, productId, RatingType.PRODUCT, point);

      rate.processAddRatePoint(point);
    } else {
      rate.processUpdateRatePoint(rating.getPoint(), point);
      rating.setPoint(point);
    }

    product.setRate(rate);
    productStorage.save(product);
    ratingStorage.save(rating);
    return rate;
  }
}