package com.salespage.salespageservice.app.responses.ProductResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salespage.salespageservice.app.responses.UploadImageData;
import com.salespage.salespageservice.app.responses.storeResponse.SellerStoreResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductCategory;
import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.entities.infor.Rate;
import com.salespage.salespageservice.domains.utils.Helper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductDetailResponse extends ProductResponse {

  @Schema(description = "Danh sách URL ảnh sản phẩm")
  List<UploadImageData> imageUrls = new ArrayList<>();

  @Schema(description = "Mô tả sản phẩm")
  String description;

  @Schema(description = "Danh sách sản phẩm tương tự")
  List<ProductResponse> similarProducts = new ArrayList<>();

  @Schema(description = "Trạng thái thích sản phẩm")
  @JsonProperty("isLike")
  Boolean isLike = false;

  @Schema(description = "Đánh giá sản phẩm")
  Float rate = 0F;

  @Schema(description = "Danh sách các cửa hàng bán")
  List<SellerStoreResponse> stores;

  @Schema(description = "ID danh mục sản phẩm")
  String categoryId;

  @Override
  public void assignFromProduct(Product product) {
    super.assignFromProduct(product);
    imageUrls = product.getImageUrls().stream()
        .map(image -> new UploadImageData(Helper.generateRandomString(), Helper.generateRandomString() + ".png", "done", image, image))
        .collect(Collectors.toList());
    categoryId = product.getCategoryId();
    description = product.getDescription();
  }

  public void assignFromCategory(ProductCategory productCategory) {
    categoryId = productCategory.getId().toHexString();
    categoryName = productCategory.getCategoryName();
  }
}
