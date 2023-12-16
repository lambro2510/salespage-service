package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.PaymentDtos.CreatePaymentDto;
import com.salespage.salespageservice.app.responses.InfoResponse;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.transactionResponse.PaymentTransactionResponse;
import com.salespage.salespageservice.domains.entities.*;
import com.salespage.salespageservice.domains.entities.status.BankStatus;
import com.salespage.salespageservice.domains.entities.status.PaymentStatus;
import com.salespage.salespageservice.domains.entities.types.NotificationType;
import com.salespage.salespageservice.domains.entities.types.PaymentType;
import com.salespage.salespageservice.domains.exceptions.BadRequestException;
import com.salespage.salespageservice.domains.exceptions.ResourceExitsException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.producer.Producer;
import com.salespage.salespageservice.domains.utils.Helper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PaymentService extends BaseService {

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
    paymentTransaction.setType(PaymentType.IN);
    paymentTransaction.setAmount(dto.getAmount());
    paymentTransaction.setDescription(Helper.genDescription(id.toHexString()));
    producer.createPaymentTransaction(paymentTransaction);
//    paymentTransactionStorage.save(paymentTransaction);
    return id.toHexString();
  }





  public PageResponse<PaymentTransactionResponse> getPayment(String username, Pageable pageable) {
    List<PaymentTransactionResponse> responses = new ArrayList<>();
    Page<PaymentTransaction> paymentTransactions = paymentTransactionStorage.findByUsernameOrderByCreatedAtDesc(username, pageable);
    List<String> bankAccountIds = paymentTransactions.stream().map(PaymentTransaction::getBankAccountId).collect(Collectors.toList());
    Map<String, BankAccount> bankAccountMap = bankAccountStorage.findBankAccountByIdIn(bankAccountIds).stream().collect(Collectors.toMap(BankAccount::getIdStr, Function.identity()));
    for (PaymentTransaction paymentTransaction : paymentTransactions.getContent()) {
      PaymentTransactionResponse paymentTransactionResponse = paymentTransaction.partnerToPaymentTransactionResponse();
      BankAccount bankAccount = bankAccountMap.get(paymentTransaction.getBankAccountId());
      if (Objects.isNull(bankAccount)) continue;
      paymentTransactionResponse.setBankAccountName(bankAccount.getBankAccountName());
      paymentTransactionResponse.setBankName(bankAccount.getBankName());
      paymentTransactionResponse.setBankAccountNo(bankAccount.getAccountNo());
      responses.add(paymentTransactionResponse);
    }
    return PageResponse.createFrom(new PageImpl<>(responses, pageable, paymentTransactions.getTotalElements()));
  }

  @Transactional
  public void cancelPayment(String username, String paymentId) {
    PaymentTransaction paymentTransaction = paymentTransactionStorage.findByIdAndUsername(paymentId, username);
    if (Objects.isNull(paymentTransaction)) throw new ResourceNotFoundException("Không tìm thấy giao dịch");
    if (paymentTransaction.getPaymentStatus().equals(PaymentStatus.RESOLVE))
      throw new ResourceExitsException("Giao dịch đã hoàn thành không thể hủy bỏ");
    paymentTransaction.setPaymentStatus(PaymentStatus.CANCEL);
    paymentTransactionStorage.save(paymentTransaction);
  }

}
