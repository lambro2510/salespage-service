package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.transactionResponse.TotalStatisticResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.entities.TransactionStatistic;
import com.salespage.salespageservice.domains.entities.types.StatisticType;
import com.salespage.salespageservice.domains.utils.Helper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionStatisticService extends BaseService{

  public void statisticToday(){
    LocalDate today = LocalDate.now();
    String date = today.getYear() + "-" + today.getMonthValue() + "-" + today.getDayOfMonth();
    List<ProductTransaction> listProductTransactions = productTransactionStorage.findByCreatedAtBetween(Helper.getStartTimeOfDay(today), Helper.getEndTimeOfDay(today));
    for(ProductTransaction transaction : listProductTransactions){
      TransactionStatistic transactionStatistic = transactionStatisticStorage.findByDateAndProductId(date, transaction.getProductId());
      if(Objects.isNull(transactionStatistic)) transactionStatistic = new TransactionStatistic();

      TotalStatisticResponse total = productTransactionStorage.countByProductId(transaction.getProductId(), today);
      transactionStatistic.setStatisticType(StatisticType.DAY);
      transactionStatistic.setDate(date);
      transactionStatistic.setUsername(transaction.getSellerUsername());
      transactionStatistic.setProductId(transaction.getProductId());
      transactionStatistic.setTotalPrice(total.getTotalPrice());
      transactionStatistic.setTotalProduct(total.getQuantity());
      transactionStatistic.setTotalUser(productTransactionStorage.countUserBuy(transaction.getBuyerUsername(), transaction.getProductId()));
      transactionStatisticStorage.save(transactionStatistic);
    }

  }
}
