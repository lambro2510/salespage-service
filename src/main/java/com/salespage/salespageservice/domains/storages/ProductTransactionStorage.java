package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.producer.Producer;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductTransactionStorage extends BaseStorage {

    public void save(ProductTransaction productTransaction) {
        productTransactionRepository.save(productTransaction);
    }

    public ProductTransaction findProductTransactionByIdInCache(String id) {
        return productTransactionRepository.findProductTransactionById(new ObjectId((id)));
    }
}
