package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.salespage.salespageservice.app.responses.BankResponse.BankAccountResponse;
import com.salespage.salespageservice.domains.config.ObjectIdDeserializer;
import com.salespage.salespageservice.domains.config.ObjectIdSerializer;
import com.salespage.salespageservice.domains.entities.status.BankStatus;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Data
@Document("bank_account")
public class BankAccount extends BaseEntity {

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  @JsonDeserialize(using = ObjectIdDeserializer.class)
  private ObjectId id;

  private String username;

  @Field("account_no")
  private String accountNo;

  @Field("bank_id")
  private Long bankId;

  @Field("bank_id_bin")
  private String bin;

  @Field("bank_name")
  private String bankName;

  @Field("bank_logo_url")
  private String bankLogoUrl;

  @Field("bank_full_name")
  private String bankFullName;

  @Field("bank_account_name")
  private String bankAccountName;

  private BankStatus status;

  @Field("money_in")
  private Double moneyIn = 0D;

  @Field("money_out")
  private Double moneyOut = 0D;

  public String getIdStr() {
    return id.toHexString();
  }

  public BankAccountResponse assignToBankAccountResponse() {
    BankAccountResponse bankAccountResponse = new BankAccountResponse();
    bankAccountResponse.assignFromBankAccount(this);
    return bankAccountResponse;
  }
}
