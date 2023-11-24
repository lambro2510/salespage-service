package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.domains.Constants;
import com.salespage.salespageservice.domains.entities.StatisticCheckpoint;
import com.salespage.salespageservice.domains.entities.TpBankTransaction;
import com.salespage.salespageservice.domains.info.TpBankTransactionData;
import com.salespage.salespageservice.domains.utils.DateUtils;
import com.salespage.salespageservice.domains.utils.RequestUtil;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Log4j2
public class TpBankService extends BaseService {

  @Value("${tp-bank.account-no}")
  private String TPBANKACCOUNTNO;

  @Value("${tp-bank.api.url}")
  private String TPBANKURL;

  public TpBankTransactionData getBankTransaction(String fromDate, String toDate) throws Exception {
    String token = bankAccountStorage.getTokenFromRemoteCache();
    Map<String, String> header = new HashMap<>();
    header.put("Authorization", "Bearer " + token);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("accountNo", TPBANKACCOUNTNO);
    jsonObject.put("currency", "VND");
    jsonObject.put("fromDate", fromDate);
    jsonObject.put("keyword", "");
    jsonObject.put("toDate", toDate);
    return RequestUtil.request(HttpMethod.POST,
        TPBANKURL + "/api/smart-search-presentation-service/v1/account-transactions/find",
        TpBankTransactionData.class,
        jsonObject,
        header
    );
  }

  public void saveTpBankTransactionToday() throws Exception {
    LocalDateTime now = DateUtils.startOfDay();
    String fromDate = DateUtils.convertLocalDateTimeToString(now.minusDays(1), "yyyyMMdd");
    String toDate = DateUtils.convertLocalDateTimeToString(now, "yyyyMMdd");
    TpBankTransactionData tpBankTransactionData = getBankTransaction(fromDate, toDate);
    log.info(tpBankTransactionData);
    for (TpBankTransactionData.TpBankTransactionInfo info : tpBankTransactionData.getTransactionInfos()) {
      TpBankTransaction tpBankTransaction = tpBankTransactionStorage.findByTransId(info.getId());
      if (Objects.isNull(tpBankTransaction)) {
        tpBankTransaction = new TpBankTransaction();
        tpBankTransaction.fromTpBankTransactionInfo(info);
      }
      tpBankTransactionStorage.save(tpBankTransaction);
    }
  }

  public void saveTpBankTransactionPeriodDay() throws Exception {
    StatisticCheckpoint statisticCheckpoint = statisticCheckpointStorage.findById(Constants.TRANSACTION_CHECKPOINT_ID);
    if (Objects.isNull(statisticCheckpoint)) {
      statisticCheckpoint = new StatisticCheckpoint();
      statisticCheckpoint.setId(Constants.TRANSACTION_CHECKPOINT_ID);
      statisticCheckpoint.setCheckPoint(DateUtils.startOfDay().minusDays(64));
    }
    LocalDateTime currentDate = statisticCheckpoint.getCheckPoint();
    while (currentDate.isBefore((DateUtils.startOfDay()))) {
      String fromDate = DateUtils.convertLocalDateTimeToString(currentDate, "yyyyMMdd");
      String toDate = DateUtils.convertLocalDateTimeToString(currentDate.plusDays(1), "yyyyMMdd");
      TpBankTransactionData tpBankTransactionData = getBankTransaction(fromDate, toDate);
      for (TpBankTransactionData.TpBankTransactionInfo info : tpBankTransactionData.getTransactionInfos()) {
        TpBankTransaction tpBankTransaction = tpBankTransactionStorage.findByTransId(info.getId());
        if (Objects.isNull(tpBankTransaction)) {
          tpBankTransaction = new TpBankTransaction();
          tpBankTransaction.fromTpBankTransactionInfo(info);
        }
        tpBankTransactionStorage.save(tpBankTransaction);
      }
      currentDate = currentDate.plusDays(1);
      statisticCheckpoint.setCheckPoint(currentDate);
      statisticCheckpointStorage.save(statisticCheckpoint);
    }
  }


}
