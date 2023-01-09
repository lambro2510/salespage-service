package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.productDtos.ProductDto;
import com.salespage.salespageservice.app.dtos.productDtos.ProductInfoDto;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ProductService extends BaseService {

    public ResponseEntity<Product> createProduct(String username, ProductInfoDto dto) {
        Product product = new Product();
        product.updateProduct(dto);
        product.setSellerUsername(username);

        productStorage.save(product);
        return ResponseEntity.ok(product);
    }

    public ResponseEntity<Product> updateProduct(String username, ProductDto dto) {
        Product product = productStorage.findProductById(dto.getProductId());
        if (Objects.isNull(product)) throw new ResourceNotFoundException("product not exist");

        product.updateProduct(dto);
        return ResponseEntity.ok(product);
    }

    public ResponseEntity<PageResponse<Product>> getAllProduct(Pageable pageable) {
        Page<Product> productPage = productStorage.findAllProduct(pageable);
        return ResponseEntity.ok(PageResponse.createFrom(productPage));
    }

    public ResponseEntity<Product> getProductDetail(String productId) {
        return ResponseEntity.ok(productStorage.findProductById(productId));
    }
}
