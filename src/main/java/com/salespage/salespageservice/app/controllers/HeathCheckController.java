package com.salespage.salespageservice.app.controllers;// package com.salespage.salespageservice.app.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("")
public class HeathCheckController {
  @Value("${url.exchange}")
  private String ExchangeUrl;

//  @Autowired
//  private GitProperties gitProperties;


//  @GetMapping("")
//  public ResponseEntity<?> checkExchangeMoney() throws IOException {
//    String data = RequestUtil.request(ExchangeUrl);
//    data = data.substring(data.indexOf("["), data.lastIndexOf("]") + 1);
//    return ResponseEntity.ok(JsonParser.arrayList(data, ExchangeMoney.class));
//  }

//  @GetMapping("")
//  public ResponseEntity<?> checkExchangeMoney() throws IOException {
//    String gitVersion = gitProperties.getShortCommitId();
//    return ResponseEntity.ok("Git Version: " + gitVersion);
//  }
}
