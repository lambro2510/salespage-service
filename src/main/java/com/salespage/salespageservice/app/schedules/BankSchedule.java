package com.salespage.salespageservice.app.schedules;

import com.salespage.salespageservice.domains.services.BankService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class BankSchedule {

  @Autowired
  private BankService bankService;

  public void getOath2TokenFromCasso(){
    log.info("-----getOath2TokenFromCasso-----start");
    bankService.getOath2Token();
    log.info("-----getOath2TokenFromCasso-----end");
  }
}
