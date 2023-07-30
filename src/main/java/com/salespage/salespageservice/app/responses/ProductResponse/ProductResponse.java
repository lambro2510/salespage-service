package com.salespage.salespageservice.app.responses.ProductResponse;

import com.salespage.salespageservice.domains.entities.Product;
import lombok.Data;

@Data
public class ProductResponse extends ProductDataResponse {

    protected String imageUrl;

    protected Boolean isHot = false;

    @Override
    public void assignFromProduct(Product product) {
        super.assignFromProduct(product);
        imageUrl = product.getDefaultImageUrl();
    }
}
