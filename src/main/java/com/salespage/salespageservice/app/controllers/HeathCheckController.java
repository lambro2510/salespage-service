package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.domains.datas.ExchangeMoney;
import com.salespage.salespageservice.domains.utils.JsonParser;
import com.salespage.salespageservice.domains.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("")
public class HeathCheckController {
  @Value("${url.exchange}")
  private String ExchangeUrl;

  @GetMapping("")
  public ResponseEntity<?> checkExchangeMoney() throws IOException {
    String data = RequestUtil.request(ExchangeUrl);
    data = data.substring(data.indexOf("["), data.lastIndexOf("]") + 1);
    return ResponseEntity.ok(JsonParser.arrayList(data, ExchangeMoney.class));
  }
}
