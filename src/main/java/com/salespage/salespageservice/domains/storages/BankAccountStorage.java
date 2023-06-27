package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.BankAccount;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankAccountStorage extends BaseStorage {
    public BankAccount findBankAccountById(String bankAccountId) {
        return bankAccountRepository.findBankAccountById(bankAccountId);
    }

    public void save(BankAccount bankAccount) {
        bankAccountRepository.save(bankAccount);
    }

    public BankAccount findByUsernameAndBankIdAndAccountNo(String username, Long bankId, String accountNumber) {
        return bankAccountRepository.findByUsernameAndBankIdAndAccountNo(username, bankId, accountNumber);
    }

    public BankAccount findByBankIdAndAccountNo(Long bankId, String accountNumber) {
        return bankAccountRepository.findByBankIdAndAccountNo(bankId, accountNumber);
    }

    public List<BankAccount> findBankAccountByIdIn(List<String> bankAccountIds) {
        return bankAccountRepository.findBankAccountByIdIn(bankAccountIds);
    }
}
