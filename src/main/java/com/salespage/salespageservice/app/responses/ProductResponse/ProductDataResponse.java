package com.salespage.salespageservice.app.responses.ProductResponse;

import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.infor.Rate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDataResponse {

  @Schema(description = "ID sản phẩm")
  protected String productId;

  @Schema(description = "Tên sản phẩm")
  protected String productName;

  @Schema(description = "Giá sản phẩm")
  protected Double productPrice;

  @Schema(description = "Tên danh mục sản phẩm")
  protected String categoryName;

  @Schema(description = "Mô tả sản phẩm")
  protected String description;

  @Schema(description = "Danh sách loại sản phẩm")
  protected List<String> productTypes = new ArrayList<>();

  @Schema(description = "Đánh giá sản phẩm")
  protected Rate productRate;

  @Schema(description = "Tên người bán")
  protected String sellerUsername;

  @Schema(description = "Giảm giá phần trăm")
  protected Double discountPercent;

  @Schema(description = "Tiền sau giảm giá")
  protected Double sellPrice;

  public void assignFromProduct(Product product) {
    productId = product.getId().toHexString();
    productName = product.getProductName();
    productPrice = product.getPrice();
    sellerUsername = product.getSellerUsername();
    productRate = product.getRate();
    description = product.getDescription();
    discountPercent = product.getDiscountPercent();
    sellPrice = product.getSellPrice();
  }
}
