package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.productDtos.*;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductDetailResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductItemResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductTypeResponse;
import com.salespage.salespageservice.app.responses.UploadImageData;
import com.salespage.salespageservice.domains.entities.*;
import com.salespage.salespageservice.domains.entities.status.ProductTypeStatus;
import com.salespage.salespageservice.domains.entities.types.FavoriteType;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.BadRequestException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

  public Product updateProduct(String username, ProductDto dto) {
    Product product = productStorage.findProductById(dto.getProductId());
    if (Objects.isNull(product)) throw new ResourceNotFoundException("Không tồn tại sản phẩm này hoặc đã bị xóa");
    if (!Objects.equals(product.getSellerUsername(), username))
      throw new AuthorizationException("Bạn không có quyền cập nhật sản phẩm này");

    ProductCategory productCategory = productCategoryStorage.findByCreatedByAndId(username, dto.getCategoryId());
    if(Objects.isNull(productCategory)) throw new ResourceNotFoundException("Không tồn tại danh mục này");

    SellerStore sellerStore = sellerStoreStorage.findById(dto.getStoreId());
    if(Objects.isNull(sellerStore)) throw new ResourceNotFoundException("Không tồn tại danh mục này");

    product.updateProductInfo(dto);
    productStorage.save(product);
    return product;
  }

  public PageResponse<ProductItemResponse> getAllProduct(String sellerUsername, String productId,  String productName, Long minPrice, Long maxPrice, String storeName, String username, Long lte, Long gte, Pageable pageable) {

    Query query = new Query();
    if (StringUtil.isNotBlank(sellerUsername)) {
      query.addCriteria(Criteria.where("seller_username").is(sellerUsername));
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

    if (StringUtil.isNotBlank(username)) {
      List<SellerStore> sellerStores = sellerStoreService.findIdsByOwnerStoreName(username);
      List<String> ids = sellerStores.stream()
          .map(s -> s.getId().toHexString())
          .collect(Collectors.toList());
      query.addCriteria(Criteria.where("seller_store_id").in(ids));
    }

    Page<Product> productPage = productStorage.findAll(query, pageable);
    List<ProductItemResponse> products = productPage.getContent().stream().map(Product::assignToProductItemResponse).collect(Collectors.toList());


    Map<String, List<Product>> productsByStoreId = productPage.getContent().stream()
        .collect(Collectors.groupingBy(Product::getSellerStoreId));
    List<String> listStoreId = new ArrayList<>(productsByStoreId.keySet());
    List<SellerStore> sellerStores = sellerStoreStorage.findByIdIn(Helper.convertListStringToListObjectId(listStoreId));
    Map<ObjectId, SellerStore> sellerStoreMap = sellerStores.stream().collect(Collectors.toMap(SellerStore::getId, Function.identity()));
    for (ProductItemResponse response : products) {
      ProductCategory productCategory = productCategoryStorage.findById(response.getCategoryId());
      if(Objects.isNull(productCategory)) throw new ResourceNotFoundException("Không tìm thấy danh mục sản phẩm");
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

  public ProductDetailResponse getProductDetail(String username, String productId) throws Exception {
    ProductDetailResponse response = new ProductDetailResponse();
    Product product = productStorage.findProductById(productId);
    response = product.assignToProductDetailResponse();
    SellerStore sellerStore = sellerStoreStorage.findById(product.getSellerStoreId());
    UserFavorite userFavorite = new UserFavorite();
    if (Objects.nonNull(username)) {
      userFavorite = userFavoriteStorage.findByUsernameAndRefIdAndFavoriteType(username, productId, FavoriteType.PRODUCT);
    }

    //assign from store
    response.setStoreName(sellerStore.getStoreName());
    response.setStoreId(sellerStore.getId().toHexString());
    response.setStoreImageUrl(sellerStore.getStoreName());
    response.setStoreRate(sellerStore.getRate());

    //assign from favorite
    response.setIsLike(userFavorite.getIsLike());
    response.setRate(userFavorite.getRateStar());
    List<Product> similarProducts = findSimilarProducts(product);
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

  public List<UploadImageData> uploadProductImage(String username, String productId, MultipartFile file) throws IOException {
    List<UploadImageData> imageUrls = new ArrayList<>();
    Product product = productStorage.findProductById(productId);
    if (product == null) throw new ResourceNotFoundException("Không tòn tại sản phẩm này hoặc đã bị xóa");
    if (!product.getSellerUsername().equals(username))
      throw new AuthorizationException("Không được phép");

      String imageUrl = googleDriver.uploadPublicImageNotDelete("Product-" + productId, file.getName() + System.currentTimeMillis(), Helper.convertMultiPartToFile(file));
      product.getImageUrls().add(imageUrl);
      imageUrls.add(new UploadImageData(file.getName(), "done", imageUrl, imageUrl));

    product.setDefaultImageUrl(imageUrl);
    productStorage.save(product);
    return (imageUrls);
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

  private List<Product> findSimilarProducts(Product product) throws Exception {
    List<ProductTypeDetail> typeDetails = productTypeStorage.findByProductId(product.getId().toHexString());
    List<Product> products;
    if (Objects.nonNull(typeDetails) && !typeDetails.isEmpty()) {
      List<String> listType = typeDetails.stream()
          .map(ProductTypeDetail::getTypeDetailName)
          .collect(Collectors.toList());

      List<ProductTypeDetail> similarType = productTypeStorage.getTop10SimilarProduct(listType);
      products = productStorage.findByIdIn(similarType.stream().map(ProductTypeDetail::getProductId).collect(Collectors.toList()));
    } else {
      products = productStorage.findTop10ByCategoryIdOrderByCreatedAtDesc(product.getCategoryId());
    }
    return products;
  }

}