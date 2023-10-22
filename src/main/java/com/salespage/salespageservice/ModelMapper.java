package com.salespage.salespageservice;

import com.salespage.salespageservice.app.dtos.ProductCategories.ProductCategoryDto;
import com.salespage.salespageservice.app.dtos.productComboDtos.ComboDto;
import com.salespage.salespageservice.app.dtos.productDtos.ProductDetailDto;
import com.salespage.salespageservice.app.dtos.productDtos.ProductDto;
import com.salespage.salespageservice.app.dtos.storeDtos.SellerStoreDto;
import com.salespage.salespageservice.app.responses.ProductComboResponse.ProductComboDetailResponse;
import com.salespage.salespageservice.app.responses.ProductComboResponse.ProductComboResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.*;
import com.salespage.salespageservice.app.responses.storeResponse.SellerStoreResponse;
import com.salespage.salespageservice.domains.entities.*;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ModelMapper {

  @Named("objectIdToString")
  default String objectIdToString(ObjectId objectId) {
    return objectId != null ? objectId.toHexString() : null;
  }

  @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
  ProductComboResponse toProductComboResponse(ProductCombo productCombo);

  List<ProductComboResponse> toListProductCombo(List<ProductCombo> productCombos);

  ProductCombo toProductCombo(ComboDto dto);

  Product toProduct(ProductDto dto);

  // Custom mapping for ProductCategory
  @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
  ProductCategoryResponse toProductCategoryResponse(ProductCategory category);

  List<ProductCategoryResponse> toListProductCategoryResponse(List<ProductCategory> categories);

  ProductCategory toProductCategory(ProductCategoryDto dto);

  SellerStore toSellerStore(SellerStoreDto dto);

  @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
  SellerStoreResponse toSellerStoreResponse(SellerStore sellerStore);

  List<SellerStoreResponse> toListSellerStoreResponse(List<SellerStore> sellerStoreList);

  @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
  SellerProductResponse toSellerProductResponse(Product product);

  List<SellerProductResponse> toListSellerProductResponse(List<Product> products);

  @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
  SellerProductDetailResponse toSellerProductDetailResponse(Product product);

  TypeDetailResponse toTypeDetails(ProductTypeDetail typeDetail);

  List<TypeDetailResponse> toListTypeDetails(List<ProductTypeDetail> typeDetails);

  @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
  ProductComboDetailResponse toProductComboDetailResponse(ProductCombo bestCombo);

  ProductDetail toProductDetail(ProductDetailDto dto);

  void mapToProductCategory(ProductCategoryDto dto, @MappingTarget ProductCategory productCategory);

  void mapToProductCombo(ComboDto dto, @MappingTarget ProductCombo productCombo);

  void mapToProduct(ProductDto dto, @MappingTarget Product product);

  void mapToSellerStore(SellerStoreDto dto,@MappingTarget  SellerStore sellerStore);

  void mapToProductDetailDto(ProductDetailDto dto,@MappingTarget ProductDetail productDetail);

  @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
  ProductDetailInfoResponse toProductDetailInfo(ProductDetail productDetails);

  List<ProductDetailInfoResponse> toListProductDetailInfo(List<ProductDetail> productDetails);
}
