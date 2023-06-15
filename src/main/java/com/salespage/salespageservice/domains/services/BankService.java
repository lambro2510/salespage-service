package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.bankDtos.BankDto;
import com.salespage.salespageservice.app.dtos.bankDtos.TransactionData;
import com.salespage.salespageservice.domains.entities.BankTransaction;
import com.salespage.salespageservice.domains.entities.PaymentTransaction;
import com.salespage.salespageservice.domains.entities.User;
import com.salespage.salespageservice.domains.entities.status.PaymentStatus;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.utils.Helper;
import com.salespage.salespageservice.domains.utils.RequestUtil;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Log4j2
public class BankService extends BaseService{
  @Value("${casso.bank-acc-id}")
  private String BANKACCID;

  @Value("${casso.apikey}")
  private String APIKEY;

  @Value("${casso.url}")
  private String URL;
  public void receiveBankTransaction(BankDto bankDto) {
    List<BankTransaction> bankTransactions = new ArrayList<>();
    for(TransactionData data : bankDto.getData()){
      BankTransaction bankTransaction = new BankTransaction();
      bankTransaction.partnerFromTransactionData(data);
      bankTransactions.add(bankTransaction);
    }
    if(!bankTransactions.isEmpty()){
      bankTransactionStorage.saveAll(bankTransactions);
    }
  }

  public List<BankTransaction> getAllTransaction() {
    return bankTransactionStorage.findAll();
  }

  public String genTransactionQr() {
    return "";
  }

  public void asyncTransaction() {
    JSONObject json = new JSONObject();
    json.put("bank_acc_id", BANKACCID);
    Map<String, String> header = new HashMap<>();
    header.put("Authorization", "Apikey " +  APIKEY);
    String response  = RequestUtil.request(HttpMethod.POST, URL + "/v2/sync", json, header).toString();
    log.info(response);
  }

  public String createPayment(String username) {
    PaymentTransaction paymentTransaction = new PaymentTransaction();
    ObjectId id = new ObjectId();
    paymentTransaction.setId(id);
    paymentTransaction.setUsername(username);
    paymentTransaction.setPaymentStatus(PaymentStatus.WAITING);
    paymentTransactionStorage.save(paymentTransaction);
    return id.toHexString();
  }

  @Transactional
  public String confirmPayment(String username, String paymentId) throws Exception {
    User user = userStorage.findByUsername(username);
    if(Objects.isNull(user)) throw new ResourceNotFoundException("Không tồn tại người dùng này");
    PaymentTransaction paymentTransaction = paymentTransactionStorage.findByIdAndUsernameAndPaymentStatus(paymentId, username, PaymentStatus.WAITING);
    if(Objects.isNull(paymentTransaction)) throw new Exception("Giao dịch không tồn tại hoặc đã được thanh toán");
    else {
      BankTransaction bankTransaction = bankTransactionStorage.findByDescription(Helper.genDescription(username, paymentId));
      if(Objects.isNull(bankTransaction)) return "Giao dịch đang được xử lý";
      if(user.updateBalance(bankTransaction.getAmount())){

        paymentTransaction.setPaymentStatus(PaymentStatus.RESOLVE);
        String message;
        if (bankTransaction.getAmount() >= 0) {
          message = "Tài khoản của bạn được cộng " + bankTransaction.getAmount();
        } else {
          message = "Tài khoản của bạn bị trừ " + Math.abs(bankTransaction.getAmount());
        }
        paymentTransaction.setDescription(message);

        paymentTransactionStorage.save(paymentTransaction);
        userStorage.save(user);
      }else{
        paymentTransaction.setPaymentStatus(PaymentStatus.CANCEL);
        paymentTransaction.setDescription("Giao dịch đã bị hủy bỏ do tài khoản của bạn không đủ tiền");
        paymentTransactionStorage.save(paymentTransaction);
      }
    }

    return "Xử lý giao dịch thành công";
  }
}