package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.PaymentStatistic;
import com.salespage.salespageservice.domains.services.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PaymentStatisticStorage extends BaseStorage {
  public void save(PaymentStatistic paymentStatistic) {
    paymentStatisticRepository.save(paymentStatistic);
  }
}
