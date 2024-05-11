package com.salespage.salespageservice.domains.entities.infor;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
public class Rate {

  @Field("total_point")
  private Float totalPoint = 0F;  //Tổng điểm đánh giá

  @Field("total_rate")
  private Float totalRate = 0F;   //Số lượng đánh giá

  @Field("avg_point")
  private Float avgPoint = 0F;    //Điểm đánh giá trung bình

  public void processAddRatePoint(Float point) {
    this.totalRate += 1;
    this.totalPoint += point;
    this.avgPoint = totalPoint / totalRate;
  }

  public void processUpdateRatePoint(Float oldPoint, Float point) {
    this.totalPoint = this.totalPoint - oldPoint + point;
    this.avgPoint = totalPoint / totalRate;

  }
}