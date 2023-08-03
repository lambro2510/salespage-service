package com.salespage.salespageservice.domains.datas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExchangeMoney {
  private String type;

  @JsonProperty("imageurl")
  private String imageUrl;

  @JsonProperty("muatienmat")
  private String BuyByMoney;

  @JsonProperty("muack")
  private String BuyByCard;

  @JsonProperty("bantienmat")
  private String SellByMoney;

  @JsonProperty("banck")
  private String SellByCard;
}
