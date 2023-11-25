package com.salespage.salespageservice.app.controllers;// package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.domains.datas.ExchangeMoney;
import com.salespage.salespageservice.domains.utils.JsonParser;
import com.salespage.salespageservice.domains.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.GitProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("")
public class HeathCheckController {
  @Value("${url.exchange}")
  private String ExchangeUrl;

  private final GitProperties gitProperties;

  public HeathCheckController(GitProperties gitProperties) {
    this.gitProperties = gitProperties;
  }

//  @GetMapping("")
//  public ResponseEntity<?> checkExchangeMoney() throws IOException {
//    String data = RequestUtil.request(ExchangeUrl);
//    data = data.substring(data.indexOf("["), data.lastIndexOf("]") + 1);
//    return ResponseEntity.ok(JsonParser.arrayList(data, ExchangeMoney.class));
//  }

  @GetMapping("")
  public ResponseEntity<?> checkExchangeMoney() throws IOException {
    String gitVersion = gitProperties.getShortCommitId();
    return ResponseEntity.ok("Git Version: " + gitVersion);
  }
}
