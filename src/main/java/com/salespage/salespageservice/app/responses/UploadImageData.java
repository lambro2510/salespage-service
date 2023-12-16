package com.salespage.salespageservice.app.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadImageData {

  String uid;

  String name;

  String status;

  String url;

  String thumbUrl;

  public UploadImageData(String url) {
    uid = UUID.randomUUID().toString();
    name = uid;
    status = "done";
    this.url = url;
    this.thumbUrl = url;
  }
}
