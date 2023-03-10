package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.salespage.salespageservice.app.dtos.productDtos.ProductInfoDto;
import com.salespage.salespageservice.domains.entities.types.ProductType;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document("product")
@Data
public class Product {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Field("product_name")
    private String productName;

    @Field("description")
    private String description;

    @Field("image_url")
    private List<String> imageUrls;

    @Field("product_type")
    private ProductType type;
    @Field("price")
    private Double price;

    @Field("selling_address")
    private String sellingAddress;

    @Field("seller_id")
    private String sellerUsername;

    public void updateProduct(ProductInfoDto dto) {
        productName = dto.getProductName();
        description = dto.getDescription();
        type = dto.getType();
        price = dto.getPrice();
        sellingAddress = dto.getSellingAddress();
    }
}
