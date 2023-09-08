//package com.salespage.salespageservice.app.schedules;
//
//import com.salespage.salespageservice.domains.services.ProductTransactionService;
//import com.salespage.salespageservice.domains.services.ShipperService;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//@Component
//@Log4j2
//public class TransactionSchedule {
//
//  @Autowired
//  ShipperService shipperService;
//
//  @Scheduled(fixedDelay = 1000 * 30) //30s 1 lần
//  private void findShipperForTransaction() {
//    log.info("findShipperForTransaction -> {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")));
//    shipperService.findShipperForProduct();
//  }
//}
