package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.salespage.salespageservice.app.responses.Statistic.TotalProductStatisticResponse;
import com.salespage.salespageservice.domains.config.ObjectIdDeserializer;
import com.salespage.salespageservice.domains.config.ObjectIdSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document("product_statistic")
@Data
@CompoundIndexes({
    @CompoundIndex(name = "unique_product_detail_id_daily", def = "{'product_detail_id' : 1, 'daily' : 2}", unique = true)
})
public class ProductStatistic {
  @Id
  private ObjectId id;

  @Field("daily")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  private LocalDate daily;

  @Field("product_detail_id")
  private String productDetailId;

  @Field("product_id")
  private String productId;

  @Field("total_user")
  private Long totalUser = 0L;

  @Field("total_buy")
  private Long totalBuy = 0L;

  @Field("total_product")
  private Long totalProduct = 0L;

  @Field("total_view")
  private Long totalView = 0L;

  @Field("total_purchase")
  private Long totalPurchase = 0L;

  @Field("total_shipper_cod")
  private Long totalShipperCod = 0L;

  public void partnerFromStatistic(TotalProductStatisticResponse statisticResponse) {
    totalUser = statisticResponse.getTotalUser() != null ?  statisticResponse.getTotalUser() : 0;
    totalProduct = statisticResponse.getTotalProduct() != null ?  statisticResponse.getTotalProduct() : 0;
    totalBuy = statisticResponse.getTotalBuy()  != null ?  statisticResponse.getTotalBuy() : 0;;
    totalPurchase = statisticResponse.getTotalPurchase()  != null ?  statisticResponse.getTotalPurchase() : 0;;
    totalShipperCod = statisticResponse.getTotalShipCod()  != null ?  statisticResponse.getTotalShipCod() : 0;;
  }
}
