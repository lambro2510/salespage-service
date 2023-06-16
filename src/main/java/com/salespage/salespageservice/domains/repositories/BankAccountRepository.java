package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.BankAccount;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends MongoRepository<BankAccount, ObjectId> {
  BankAccount findBankAccountById(String bankAccountId);

  BankAccount findByUsernameAndBankIdAndAccountNo(String username, Long bankId, String accountNumber);

  BankAccount findByBankIdAndAccountNo(Long bankId, String accountNumber);
}
