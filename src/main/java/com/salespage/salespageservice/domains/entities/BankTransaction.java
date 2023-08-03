package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.salespage.salespageservice.app.dtos.bankDtos.TransactionData;
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
  @Field("_id")
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId id;

  @Field("tid")
  private String tid;

  @Field("description")
  private String description;

  @Field("amount")
  private Long amount;

  @Field("cusum_balance")
  private Integer cusumBalance;

  @Field("when")
  private String when;

  @Field("bank_sub_acc_id")
  private String bankSubAccId;

  @Field("sub_acc_id")
  private String subAccId;

  @Field("virtual_account")
  private String virtualAccount;

  @Field("virtual_account_name")
  private String virtualAccountName;

  @Field("corresponsive_name")
  private String corresponsiveName;

  @Field("corresponsive_account")
  private String corresponsiveAccount;

  @Field("corresponsive_bank_id")
  private String corresponsiveBankId;

  @Field("corresponsive_bank_name")
  private String corresponsiveBankName;

  @Field("created_at")
  private LocalDateTime createdAt = LocalDateTime.now();

  public void partnerFromTransactionData(TransactionData transactionData) {
    setTid(transactionData.getTid());
    setDescription(transactionData.getDescription().replaceAll(" ", ""));
    setAmount(transactionData.getAmount());
    setCusumBalance(transactionData.getCusumBalance());
    setWhen(transactionData.getWhen());
    setBankSubAccId(transactionData.getBankSubAccId());
    setSubAccId(transactionData.getSubAccId());
    setVirtualAccount(transactionData.getVirtualAccount());
    setVirtualAccountName(transactionData.getVirtualAccountName());
    setCorresponsiveName(transactionData.getCorresponsiveName());
    setCorresponsiveAccount(transactionData.getCorresponsiveAccount());
    setCorresponsiveBankId(transactionData.getCorresponsiveBankId());
    setCorresponsiveBankName(transactionData.getCorresponsiveBankName());
  }
}
