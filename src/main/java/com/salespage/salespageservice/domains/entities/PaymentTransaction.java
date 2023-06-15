package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.app.responses.transactionResponse.PaymentTransactionResponse;
import com.salespage.salespageservice.domains.entities.status.PaymentStatus;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;

@Document("payment_transaction")
@Data
public class PaymentTransaction extends BaseEntity{

  @Id
  private ObjectId id;

  @Field("username")
  private String username;

  @Field("description")
  private String description;

  @Field("payment_status")
  private PaymentStatus paymentStatus;

  public PaymentTransactionResponse partnerToPaymentTransactionResponse(){
    PaymentTransactionResponse response = new PaymentTransactionResponse();
    response.setPaymentId(id.toHexString());
    response.setStatus(paymentStatus);
    response.setCreated(new Date(createdAt));
    return response;
  }

}
