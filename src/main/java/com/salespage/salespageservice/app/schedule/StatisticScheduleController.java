package com.salespage.salespageservice.app.schedule;

import com.salespage.salespageservice.domains.services.ProductStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("api/v1/it/statistic")
public class StatisticScheduleController {
  @Autowired
  private ProductStatisticService productStatisticService;

  @PostMapping("today")
  public ResponseEntity<Boolean> updatePaymentToday() {
    productStatisticService.asyncStatisticToday();
    return ResponseEntity.ok(true);
  }

  @PostMapping("pre")
  public ResponseEntity<Boolean> updatePaymentPre() {
    productStatisticService.asyncStatisticPreDay();
    return ResponseEntity.ok(true);
  }

  @PostMapping("hot")
  public ResponseEntity<Boolean> updateHotProduct() {
    productStatisticService.updateToHotProduct();
    return ResponseEntity.ok(true);
  }
}
