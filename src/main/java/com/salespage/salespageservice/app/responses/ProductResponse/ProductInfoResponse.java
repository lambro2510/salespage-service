package com.salespage.salespageservice.app.responses.ProductResponse;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductInfoResponse {
  private String id;

  private String productName;

  private String defaultImageUrl;
}
