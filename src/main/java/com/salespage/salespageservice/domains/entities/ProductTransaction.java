package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionDto;
import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionInfoDto;
import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document("product_transaction")
@Data
public class ProductTransaction extends BaseEntity {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Field("purchaser_username")
    private String purchaserUsername;

    @Field("product")
    private Product product;

    @Field("state")
    private ProductTransactionState state;

    @Field("quantity")
    private Long quantity;

    @Field("address")
    private String address;

    @Field("note")
    private String note;

    @Field("message")
    private List<Message> messages = new ArrayList<>();

    public void createNewTransaction(String username, ProductTransactionDto dto) {
        this.purchaserUsername = username;
        product = dto.getProduct();
        address = dto.getAddress();
        quantity = dto.getQuantity();
        note = dto.getNote();
        state = ProductTransactionState.WAITING;
    }

    public void updateTransaction(ProductTransactionInfoDto dto) {
        quantity = dto.getQuantity();
        note = dto.getNote();
    }

    public void updateState(ProductTransactionState state, String note) {
        this.state = state;
        this.note = note;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Message extends BaseEntity {
        private String sender;

        private String receiver;

        private String content;

    }
}
