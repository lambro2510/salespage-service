package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.salespage.salespageservice.app.dtos.bankDtos.TransactionData;
import com.salespage.salespageservice.app.responses.BankResponse.MbBankTransaction;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Document("bank_transaction")
public class BankTransaction {

  @Id
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId id;

  @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
  @Field("posting_date")
  private LocalDateTime postingDate;

  @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
  @Field("transaction_date")
  private LocalDateTime transactionDate;

  @Field("account_no")
  private String accountNo;

  @Field("credit_amount")
  private Double creditAmount;

  @Field("debit_amount")
  private Double debitAmount;

  @Field("currency")
  private String currency;

  @Field("description")
  private String description;

  @Field("available_balance")
  private Double availableBalance;

  @Field("beneficiary_account")
  private String beneficiaryAccount;

  @Field("ref_no")
  private String refNo;

  @Field("ben_account_name")
  private String benAccountName;

  @Field("bank_name")
  private String bankName;

  @Field("ben_account_no")
  private String benAccountNo;

  @Field("created_at")
  private LocalDateTime createdAt = LocalDateTime.now();

  public void partnerFromTransactionData(MbBankTransaction.Transaction transaction) {
    setPostingDate(transaction.getPostingDate());
    setTransactionDate(transaction.getTransactionDate());
    setAccountNo(transaction.getAccountNo());
    setCreditAmount(transaction.getCreditAmount());
    setDebitAmount(transaction.getDebitAmount());
    setCurrency(transaction.getCurrency());
    setDescription(transaction.getDescription());
    setAvailableBalance(transaction.getAvailableBalance());
    setBeneficiaryAccount(transaction.getBeneficiaryAccount());
    setRefNo(transaction.getRefNo());
    setBenAccountName(transaction.getBenAccountName());
    setBankName(transaction.getBankName());
    setBenAccountNo(transaction.getBenAccountNo());
  }
}
