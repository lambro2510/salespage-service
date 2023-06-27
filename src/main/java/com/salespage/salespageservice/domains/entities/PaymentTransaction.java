package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.salespage.salespageservice.app.responses.transactionResponse.PaymentTransactionResponse;
import com.salespage.salespageservice.domains.entities.status.PaymentStatus;
import com.salespage.salespageservice.domains.entities.types.PaymentType;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;

@Document("payment_transaction")
@Data
public class PaymentTransaction extends BaseEntity {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Field("username")
    private String username;

    @Field("description")
    private String description;

    @Field("amount")
    private Long amount;

    @Field("payment_type")
    private PaymentType type;

    @Field("bank_account_id")
    private String bankAccountId;

    @Field("payment_status")
    private PaymentStatus paymentStatus;

    public PaymentTransactionResponse partnerToPaymentTransactionResponse() {
        PaymentTransactionResponse response = new PaymentTransactionResponse();
        response.setPaymentId(id.toHexString());
        response.setStatus(paymentStatus);
        response.setCreated(new Date(createdAt));
        response.setAmount(amount);
        return response;
    }

    public boolean createdOneDayPeriod() {
        return createdAt < (System.currentTimeMillis() - 24 * 60 * 60 * 1000);
    }
}
