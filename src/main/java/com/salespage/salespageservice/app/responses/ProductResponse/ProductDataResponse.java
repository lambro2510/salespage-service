package com.salespage.salespageservice.app.responses.ProductResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductDetail;
import com.salespage.salespageservice.domains.entities.infor.Rate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ProductDataResponse {

  @Schema(description = "ID sản phẩm")
  protected String productId;

  @Schema(description = "Tên sản phẩm")
  protected String productName;

  @Schema(description = "Đánh giá sản phẩm")
  protected Rate productRate;

  @Schema(description = "Tên người bán")
  protected String sellerUsername;

  @Schema(description = "Tiền sau giảm giá nhỏ nhất")
  protected Double minSellPrice;

  @Schema(description = "Tiền sau giảm giá lớn nhất")
  protected Double maxSellPrice;

  @Schema(description = "Tiền trước giảm giá nhỏ nhất")
  protected Double minOriginPrice;

  @Schema(description = "Tiền trước giảm giá lớn nhất")
  protected Double maxOriginPrice;

  @Schema(description = "Phần trăm giảm lớn nhất")
  protected Double maxDiscountPercent;

  @Schema(description = "Số sản phẩm bán đươc")
  protected Long totalSell;

  @Schema(description = "Số lượt xem")
  protected Long totalView;

  @Schema(description = "URL ảnh sản phẩm")
  protected String imageUrl;

  @Schema(description = "Trạng thái sản phẩm hot")
  @JsonProperty("isHot")
  protected Boolean isHot = false;
  @Schema(description = "Ngày bắt đầu bán")
  protected Long createdAt;
  @Schema(description = "ID danh mục sản phẩm")
  String categoryId;

  public void assignFromProduct(Product product) {
    productId = product.getId().toHexString();
    productName = product.getProductName();
    sellerUsername = product.getCreatedBy();
    productRate = product.getRate();
    categoryId = product.getCategoryId();
    imageUrl = product.getDefaultImageUrl();
    createdAt = product.getCreatedAt();
  }

  public void assignFromListDetail(List<ProductDetail> productDetails) {
    if (productDetails != null && !productDetails.isEmpty()) {
      double maxDiscountPercent = 0D;
      Double minSellPrice = null;
      Double maxSellPrice = null;
      Double minOriginPrice = null;
      Double maxOriginPrice = null;

      for (ProductDetail productDetail : productDetails) {
        Double sellPrice = productDetail.getSellPrice();
        Double originPrice = productDetail.getOriginPrice();

        if (sellPrice != null) {
          if (minSellPrice == null || sellPrice < minSellPrice) {
            minSellPrice = sellPrice;
          }
          if (maxSellPrice == null || sellPrice > maxSellPrice) {
            maxSellPrice = sellPrice;
          }
        }

        if (originPrice != null) {
          if (minOriginPrice == null || originPrice < minOriginPrice) {
            minOriginPrice = originPrice;
          }
          if (maxOriginPrice == null || originPrice > maxOriginPrice) {
            maxOriginPrice = originPrice;
          }
        }

        if (sellPrice != null && originPrice != null) {
          double discountPercent = 100 - (sellPrice / originPrice) * 100;
          if (discountPercent > maxDiscountPercent) {
            maxDiscountPercent = discountPercent;
          }
        }
      }

      this.maxDiscountPercent = maxDiscountPercent;
      this.minSellPrice = minSellPrice;
      this.maxSellPrice = maxSellPrice;
      this.minOriginPrice = minOriginPrice;
      this.maxOriginPrice = maxOriginPrice;
    }
  }

}
