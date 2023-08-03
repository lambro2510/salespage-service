package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.PaymentDtos.CreatePaymentDto;
import com.salespage.salespageservice.app.responses.transactionResponse.PaymentTransactionResponse;
import com.salespage.salespageservice.domains.entities.BankAccount;
import com.salespage.salespageservice.domains.entities.PaymentTransaction;
import com.salespage.salespageservice.domains.entities.TpBankTransaction;
import com.salespage.salespageservice.domains.entities.User;
import com.salespage.salespageservice.domains.entities.status.PaymentStatus;
import com.salespage.salespageservice.domains.entities.types.NotificationMessage;
import com.salespage.salespageservice.domains.entities.types.NotificationType;
import com.salespage.salespageservice.domains.exceptions.ResourceExitsException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.producer.Producer;
import com.salespage.salespageservice.domains.utils.Helper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PaymentService extends BaseService{

  @Autowired
  NotificationService notificationService;

  @Autowired
  Producer producer;

  public String createPayment(String username, CreatePaymentDto dto) {
    BankAccount bankAccount = bankAccountStorage.findBankAccountById(dto.getBankAccountId());
    if (Objects.isNull(bankAccount)) throw new ResourceNotFoundException("Không tìm thấy ngân hàng liên kết này");
    PaymentTransaction paymentTransaction = new PaymentTransaction();
    ObjectId id = new ObjectId();
    paymentTransaction.setId(id);
    paymentTransaction.setUsername(username);
    paymentTransaction.setPaymentStatus(PaymentStatus.WAITING);
    paymentTransaction.setBankAccountId(dto.getBankAccountId());
    paymentTransaction.setAmount(dto.getAmount());
    paymentTransaction.setDescription(Helper.genDescription(username, id.toHexString()));
    producer.createPaymentTransaction(paymentTransaction);
//    paymentTransactionStorage.save(paymentTransaction);
    return id.toHexString();
  }

  @Transactional(noRollbackFor = {ResourceNotFoundException.class})
  public String confirmPayment(String username, String paymentId) throws Exception {
    try {
      User user = userStorage.findByUsername(username);
      if (Objects.isNull(user)) throw new ResourceNotFoundException("Không tồn tại người dùng này");
      PaymentTransaction paymentTransaction = paymentTransactionStorage.findByIdAndUsernameAndPaymentStatus(paymentId, username, PaymentStatus.WAITING);
      if (Objects.isNull(paymentTransaction))
        throw new ResourceNotFoundException("Giao dịch không tồn tại hoặc đã được thanh toán");
      else {
        TpBankTransaction tpBankTransaction = tpBankTransactionStorage.findByDescription(Helper.genDescription(username, paymentId));
        if (Objects.isNull(tpBankTransaction)) return "Giao dịch đang được xử lý";
        Integer amount = Integer.parseInt(tpBankTransaction.getAmount());
        if (user.updateBalance(true, amount)) {

          paymentTransaction.setPaymentStatus(PaymentStatus.RESOLVE);
          String message;
          if (Objects.equals(tpBankTransaction.getCreditDebitIndicator(), "CRDT")) {
            message = "Tài khoản của bạn được cộng " + amount;
            notificationService.createNotification(username, NotificationMessage.CHANGE_STATUS_PAYMENT_RESOLVE_IN.getTittle(), NotificationMessage.CHANGE_STATUS_PAYMENT_RESOLVE_IN.getMessage(), NotificationType.PAYMENT_TRANSACTION, paymentTransaction.getId().toHexString());
          } else {
            message = "Tài khoản của bạn bị trừ " + amount;
            notificationService.createNotification(username, NotificationMessage.CHANGE_STATUS_PAYMENT_RESOLVE_OUT.getTittle(), NotificationMessage.CHANGE_STATUS_PAYMENT_RESOLVE_OUT.getMessage(), NotificationType.PAYMENT_TRANSACTION, paymentTransaction.getId().toHexString());
          }
          paymentTransaction.setDescription(message);
          paymentTransactionStorage.save(paymentTransaction);
          userStorage.save(user);
        } else {
          paymentTransaction.setPaymentStatus(PaymentStatus.CANCEL);
          paymentTransaction.setDescription("Giao dịch đã bị hủy bỏ do tài khoản của bạn không đủ tiền");
          notificationService.createNotification(username, NotificationMessage.CHANGE_STATUS_PAYMENT_RESOLVE_OUT_ERR.getTittle(), NotificationMessage.CHANGE_STATUS_PAYMENT_RESOLVE_OUT_ERR.getMessage(), NotificationType.PAYMENT_TRANSACTION, paymentTransaction.getId().toHexString());
          paymentTransactionStorage.save(paymentTransaction);
        }
      }
    } catch (ResourceNotFoundException ex) {
      return ex.getMessage();
    }

    return "Xử lý giao dịch thành công";
  }

  public List<PaymentTransactionResponse> getPayment(String username) {
    List<PaymentTransactionResponse> responses = new ArrayList<>();
    List<PaymentTransaction> paymentTransactions = paymentTransactionStorage.findByUsername(username);
    List<String> bankAccountIds = paymentTransactions.stream().map(PaymentTransaction::getBankAccountId).collect(Collectors.toList());
    Map<String, BankAccount> bankAccountMap = bankAccountStorage.findBankAccountByIdIn(bankAccountIds).stream().collect(Collectors.toMap(BankAccount::getIdStr, Function.identity()));
    for (PaymentTransaction paymentTransaction : paymentTransactions) {
      PaymentTransactionResponse paymentTransactionResponse = paymentTransaction.partnerToPaymentTransactionResponse();
      BankAccount bankAccount = bankAccountMap.get(paymentTransaction.getBankAccountId());
      if (Objects.isNull(bankAccount)) continue;
      paymentTransactionResponse.setBankAccountName(bankAccount.getBankAccountName());
      paymentTransactionResponse.setBankName(bankAccount.getBankName());
      paymentTransactionResponse.setBankAccountNo(bankAccount.getAccountNo());
      responses.add(paymentTransactionResponse);
    }
    return responses;
  }

  @Transactional
  public void cancelPayment(String username, String paymentId) {
    PaymentTransaction paymentTransaction = paymentTransactionStorage.findByIdAndUsername(paymentId, username);
    if (Objects.isNull(paymentTransaction)) throw new ResourceNotFoundException("Không tìm thấy giao dịch");
    if (paymentTransaction.getPaymentStatus().equals(PaymentStatus.RESOLVE))
      throw new ResourceExitsException("Giao dịch đã hoàn thành không thể hủy bỏ");
    paymentTransaction.setPaymentStatus(PaymentStatus.CANCEL);
    notificationService.createNotification(username, "Hủy bỏ giao dịch", "Bạn đã hủy bỏ giao dịch với số giao dịch là: " + paymentId + ". Thông tin giao dịch: " + paymentTransaction.getDescription(), NotificationType.PAYMENT_TRANSACTION, paymentTransaction.getId().toHexString());
    paymentTransactionStorage.save(paymentTransaction);
  }
  
}
