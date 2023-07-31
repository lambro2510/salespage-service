package com.salespage.salespageservice.app.responses.ProductResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salespage.salespageservice.app.responses.UploadImageData;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductCategory;
import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.entities.infor.Rate;
import com.salespage.salespageservice.domains.utils.Helper;
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
    Boolean isLike = false;
    Float rate = 0F;
    String storeImageUrl;
    String storeId;
    String storeName;
    Rate storeRate;
    String categoryId;
    @Override
    public void assignFromProduct(Product product) {
        super.assignFromProduct(product);
        imageUrls = product.getImageUrls().stream()
            .map(image -> new UploadImageData(Helper.generateRandomString(), Helper.generateRandomString() + ".png", "done", image, image))
            .collect(Collectors.toList());
        categoryId = product.getCategoryId();
        description = product.getDescription();
    }

    public void assignFromStore(SellerStore sellerStore){
        storeId = sellerStore.getId().toHexString();
        storeName = sellerStore.getStoreName();
        storeImageUrl = sellerStore.getImageUrl();
        storeRate = sellerStore.getRate();
    }

    public void assignFromCategory(ProductCategory productCategory){
        categoryId = productCategory.getId().toHexString();
        categoryName = productCategory.getCategoryName();
    }
}
