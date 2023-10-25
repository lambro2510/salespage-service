package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.salespage.salespageservice.domains.config.ObjectIdDeserializer;
import com.salespage.salespageservice.domains.config.ObjectIdSerializer;
import com.salespage.salespageservice.domains.info.TpBankTransactionData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Document("tp_bank_transaction")
@Data
public class TpBankTransaction extends BaseEntity {

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  @JsonDeserialize(using = ObjectIdDeserializer.class)
  private ObjectId id;

  @Field("trans_id")
  private String transId;

  @Field("arrangement_id")
  private String arrangementId;

  @Field("reference")
  private String reference;

  @Field("description")
  private String description;

  @Field("category")
  private String category;

  @Field("booking_date")
  private String bookingDate;

  @Field("value_date")
  private String valueDate;

  @Field("amount")
  private String amount;

  @Field("currency")
  private String currency;

  @Field("credit_debit_indicator")
  private String creditDebitIndicator;

  @Field("running_balance")
  private String runningBalance;

  @Field("ofs_acct_no")
  private String ofsAcctNo;

  @Field("creditor_bank_name_vn")
  private String creditorBankNameVn;

  @Field("creditor_bank_name_en")
  private String creditorBankNameEn;

  @Field("is_process")
  private boolean isProcess = false;

  public void fromTpBankTransactionInfo(TpBankTransactionData.TpBankTransactionInfo info) {
    setTransId(info.getId());
    setArrangementId(info.getArrangementId());
    setReference(info.getReference());
    setDescription(info.getDescription());
    setCategory(info.getCategory());
    setBookingDate(info.getBookingDate());
    setValueDate(info.getValueDate());
    setAmount(info.getAmount());
    setCurrency(info.getCurrency());
    setCreditDebitIndicator(info.getCreditDebitIndicator());
    setRunningBalance(info.getRunningBalance());
    setOfsAcctNo(info.getOfsAcctNo());
    setCreditorBankNameVn(info.getCreditorBankNameVn());
    setCreditorBankNameEn(info.getCreditorBankNameEn());
  }
}
