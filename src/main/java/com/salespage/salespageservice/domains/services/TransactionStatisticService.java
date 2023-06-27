package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.transactionResponse.TotalStatisticResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.TransactionStatistic;
import com.salespage.salespageservice.domains.entities.types.StatisticType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionStatisticService extends BaseService{

  public void statisticToday(){
    LocalDate today = LocalDate.now();
    String date = today.getYear() + "-" + today.getMonthValue() + "-" + today.getDayOfMonth();
    List<Product> listProduct = productStorage.findAll();
    for(Product product : listProduct){
      TransactionStatistic transactionStatistic = transactionStatisticStorage.findByDate(date);
      if(Objects.isNull(transactionStatistic)) transactionStatistic = new TransactionStatistic();

      TotalStatisticResponse total = productTransactionStorage.countByProductId(product.getId(), today);
      transactionStatistic.setStatisticType(StatisticType.DAY);
      transactionStatistic.setDate(date);
      transactionStatistic.setUsername(product.getSellerUsername());
      transactionStatistic.setProductId(product.getId().toHexString());
      transactionStatistic.setTotalPrice(total.getTotalPrice());
      transactionStatisticStorage.save(transactionStatistic);
    }

  }
}
