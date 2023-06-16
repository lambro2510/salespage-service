package com.salespage.salespageservice.app.schedules;

import com.salespage.salespageservice.domains.services.BankService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class BankSchedule {

  @Autowired
  private BankService bankService;

  @Scheduled(fixedDelay = 1000 * 60 * 60) //1 h 1 lần đồng bộ
  public void getOath2TokenFromCasso(){
    log.info("-----async transaction-----start");
    bankService.asyncTransaction();
    log.info("-----async transaction-----end");
  }
}
