package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.producer.Producer;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
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

    public void saveAll(List<ProductTransaction> productTransactions) {
        productTransactionRepository.saveAll(productTransactions);
    }

  public Page<ProductTransaction> findAll(Query query, Pageable pageable) {
        return productTransactionRepository.findAll(query,pageable);
  }

  public List<ProductTransaction> findAllProductTransactionByProductId(String productId) {
    return productTransactionRepository.findAllProductTransactionByProductId(new ObjectId(productId));
  }
}
