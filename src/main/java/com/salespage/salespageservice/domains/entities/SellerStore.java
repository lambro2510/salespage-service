package com.salespage.salespageservice.domains.entities;import com.fasterxml.jackson.databind.annotation.JsonSerialize;import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;import com.salespage.salespageservice.app.dtos.storeDtos.SellerStoreDto;import com.salespage.salespageservice.domains.entities.infor.Rate;import com.salespage.salespageservice.domains.entities.status.StoreStatus;import lombok.Data;import lombok.EqualsAndHashCode;import org.bson.types.ObjectId;import org.springframework.data.annotation.Id;import org.springframework.data.mongodb.core.mapping.Document;import org.springframework.data.mongodb.core.mapping.Field;import java.util.List;@EqualsAndHashCode(callSuper = true)@Data@Document("store")public class SellerStore extends BaseEntity {    @Id    @JsonSerialize(using = ToStringSerializer.class)    private ObjectId id;    @Field("store_name")    private String storeName;    @Field("owner_store_name")    private String ownerStoreName;    @Field("address")    private String address;    @Field("description")    private String description;    @Field("store_status")    private StoreStatus status;    @Field("sell_product")    private List<String> sellProducts;    @Field("image_store_url")    private String imageStoreUrl;    @Field("rate")    private Rate rate = new Rate();    public void assignFromSellerStoreDto(SellerStoreDto sellerStoreDto) {        storeName = sellerStoreDto.getStoreName();        address = sellerStoreDto.getAddress();        description = sellerStoreDto.getDescription();        status = sellerStoreDto.getStatus();        sellProducts = sellerStoreDto.getSellProducts();    }}