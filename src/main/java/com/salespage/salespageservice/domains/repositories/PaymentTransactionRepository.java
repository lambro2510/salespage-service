package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.PaymentTransaction;
import com.salespage.salespageservice.domains.entities.status.PaymentStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentTransactionRepository extends MongoRepository<PaymentTransaction, ObjectId> {
  PaymentTransaction findByIdAndUsername(ObjectId id, String username);

  PaymentTransaction findByIdAndUsernameAndPaymentStatus(ObjectId objectId, String username, PaymentStatus status);

  List<PaymentTransaction> findByUsername(String username);
}
