package com.salespage.salespageservice.app.responses.Statistic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class TotalProductStatisticResponse {
  private String productId;
  private String productName;
  private Long totalUser = 0L;
  private Long totalPurchase = 0L;
  private Long totalProduct = 0L;
  private Long totalView = 0L;
  private Long totalBuy = 0L;
  private Long totalShipCod = 0L;
  private List<ProductDetailStatistic> productDetails = new ArrayList<>();

  @Data
  public static class ProductDetailStatistic {
    private String productDetailId;
    private String productDetailName;
    private Long totalUser = 0L;
    private Long totalPurchase = 0L;
    private Long totalView = 0L;
    private Long totalProduct = 0L;
    private Long totalBuy = 0L;
    private Long totalShipCod = 0L;
    private List<Daily> dailies = new ArrayList<>();
  }

  @Data
  public static class Daily {
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate daily;
    private Long totalUser = 0L;
    private Long totalPurchase = 0L;
    private Long totalView = 0L;
    private Long totalProduct = 0L;
    private Long totalBuy = 0L;
    private Long totalShipCod = 0L;
  }


}
