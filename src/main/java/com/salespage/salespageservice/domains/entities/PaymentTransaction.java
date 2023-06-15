package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.domains.entities.status.PaymentStatus;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document("payment_transaction")
@Data
public class PaymentTransaction extends BaseEntity{

  @Id
  private ObjectId id;

  @Field("username")
  private String username;

  @Field("payment_status")
  private PaymentStatus paymentStatus;

}
