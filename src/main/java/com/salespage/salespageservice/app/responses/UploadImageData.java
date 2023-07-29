package com.salespage.salespageservice.app.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadImageData {
  String name;

  String status;

  String url;

  String thumbUrl;
}
