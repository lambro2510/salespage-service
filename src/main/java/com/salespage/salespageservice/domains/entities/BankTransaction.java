package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.salespage.salespageservice.app.responses.BankResponse.MbBankTransaction;
import com.salespage.salespageservice.domains.config.ObjectIdDeserializer;
import com.salespage.salespageservice.domains.config.ObjectIdSerializer;
import com.salespage.salespageservice.domains.utils.DateUtils;
import com.salespage.salespageservice.domains.utils.Helper;
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
  @JsonSerialize(using = ObjectIdSerializer.class)
  @JsonDeserialize(using = ObjectIdDeserializer.class)
  private ObjectId id;

  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  @Field("posting_date")
  private LocalDateTime postingDate;

  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
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
  private Long createdAt = System.currentTimeMillis();

  public void partnerFromTransactionData(MbBankTransaction.Transaction transaction) {
    setPostingDate(DateUtils.convertToLocalDateTime(transaction.getPostingDate(), "dd/MM/yyyy HH:mm:ss"));
    setTransactionDate(DateUtils.convertToLocalDateTime(transaction.getTransactionDate(), "dd/MM/yyyy HH:mm:ss"));
    setAccountNo(transaction.getAccountNo());
    setCreditAmount(transaction.getCreditAmount());
    setDebitAmount(transaction.getDebitAmount());
    setCurrency(transaction.getCurrency());
    setDescription(Helper.getPaymentIdInDescription(transaction.getDescription()));
    setAvailableBalance(transaction.getAvailableBalance());
    setBeneficiaryAccount(transaction.getBeneficiaryAccount());
    setRefNo(transaction.getRefNo());
    setBenAccountName(transaction.getBenAccountName());
    setBankName(transaction.getBankName());
    setBenAccountNo(transaction.getBenAccountNo());
  }
}
