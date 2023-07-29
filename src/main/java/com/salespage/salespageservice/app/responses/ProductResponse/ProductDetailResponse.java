package com.salespage.salespageservice.app.responses.ProductResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salespage.salespageservice.app.responses.UploadImageData;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.infor.Rate;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class ProductDetailResponse extends ProductResponse {
    List<UploadImageData> imageUrls = new ArrayList<>();

    String description;
    List<ProductResponse> similarProducts = new ArrayList<>();

    @JsonProperty("isLike")
    Boolean isLike;

    Float rate;

    String storeImageUrl;

    String storeId;
    String storeName;

    Rate storeRate;

    @Override
    public void assignFromProduct(Product product) {
        super.assignFromProduct(product);
        imageUrls = product.getImageUrls().stream()
            .map(image -> new UploadImageData(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "done", image, image))
            .collect(Collectors.toList());
        description = product.getDescription();
    }
}
