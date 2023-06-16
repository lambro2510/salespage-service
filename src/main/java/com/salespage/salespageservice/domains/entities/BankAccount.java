package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.domains.entities.status.BankStatus;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Data
@Document("bank_account")
public class BankAccount extends BaseEntity{

  @Id
  private ObjectId id;

  private String username;

  @Field("account_no")
  private String accountNo;

  @Field("bank_id")
  private String bankId;

  @Field("bank_name")
  private String bankName;

  @Field("bank_full_name")
  private String bankFullName;

  @Field("bank_account_name")
  private String bankAccountName;

  private BankStatus status;

  @Field("money_in")
  private Double moneyIn;

  @Field("money_out")
  private Double moneyOut;
}
