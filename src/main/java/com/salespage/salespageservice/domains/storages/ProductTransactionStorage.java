package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.producer.Producer;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductTransactionStorage extends BaseStorage {

    public void save(ProductTransaction productTransaction) {
        productTransactionRepository.save(productTransaction);
    }

    public ProductTransaction findProductTransactionByIdInCache(String id) {
        return productTransactionRepository.findProductTransactionById(new ObjectId((id)));
    }

    public List<ProductTransaction> findAllProductById(String productId) {
        return productTransactionRepository.findAllProductTransactionByProduct_Id(new ObjectId(productId));
    }

    public void saveAll(List<ProductTransaction> productTransactions) {
        productTransactionRepository.saveAll(productTransactions);
    }
}
