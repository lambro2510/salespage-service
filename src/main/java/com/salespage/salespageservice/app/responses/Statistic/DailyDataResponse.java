package com.salespage.salespageservice.app.responses.Statistic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DailyDataResponse {
  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate daily;
  private Long totalUser = 0L;
  private Long totalPurchase = 0L;
  private Long totalView = 0L;
  private Long totalProduct = 0L;
  private Long totalBuy = 0L;
  private Long totalShipCod = 0L;

  public DailyDataResponse(TotalProductStatisticResponse.Daily daily){
    this.daily = daily.getDaily();
    totalUser = daily.getTotalUser();
    totalPurchase = daily.getTotalPurchase();
    totalProduct = daily.getTotalProduct();
    totalView = daily.getTotalView();
    totalBuy = daily.getTotalBuy();
    totalShipCod = daily.getTotalShipCod();
  }
}
