package com.salespage.salespageservice.app.responses.ProductResponse;

import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.infor.Rate;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDataResponse {
    protected String productId;

    protected String productName;

    protected Double productPrice;

    protected String categoryName;

    protected String description;

    protected List<String> productTypes = new ArrayList<>();

    protected Rate productRate;

    protected String sellerUsername;

    protected String storeName;

    protected String sellingAddress;

    public void assignFromProduct(Product product) {
        productId = product.getId().toHexString();
        productName = product.getProductName();
        productPrice = product.getPrice();
        sellerUsername = product.getSellerUsername();
        productRate = product.getRate();
        description = product.getDescription();
        sellingAddress = product.getSellingAddress();
    }
}
