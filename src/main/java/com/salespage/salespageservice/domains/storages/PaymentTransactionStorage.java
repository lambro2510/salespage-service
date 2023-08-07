package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.PaymentTransaction;
import com.salespage.salespageservice.domains.entities.status.PaymentStatus;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentTransactionStorage extends BaseStorage {
  public void save(PaymentTransaction paymentTransaction) {
    paymentTransactionRepository.save(paymentTransaction);
  }

  public PaymentTransaction findByIdAndUsername(String paymentId, String username) {
    return paymentTransactionRepository.findByIdAndUsername(new ObjectId(paymentId), username);
  }

  public PaymentTransaction findByIdAndUsernameAndPaymentStatus(String paymentId, String username, PaymentStatus status) {
    return paymentTransactionRepository.findByIdAndUsernameAndPaymentStatus(new ObjectId(paymentId), username, status);
  }

  public Page<PaymentTransaction> findByUsernameOrderByCreatedAtDesc(String username, Pageable pageable) {
    return paymentTransactionRepository.findByUsernameOrderByCreatedAtDesc(username, pageable);
  }

  public List<PaymentTransaction> findByPaymentStatus(PaymentStatus paymentStatus) {
    return paymentTransactionRepository.findByPaymentStatus(paymentStatus);
  }
}
