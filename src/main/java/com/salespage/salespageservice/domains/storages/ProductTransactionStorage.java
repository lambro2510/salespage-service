package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.app.responses.transactionResponse.TotalStatisticResponse;
import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import com.salespage.salespageservice.domains.utils.Helper;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
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
    return productTransactionRepository.findAll(query, pageable);
  }

  public List<ProductTransaction> findAllProductTransactionByProductId(String productId) {
    return productTransactionRepository.findAllProductTransactionByProductId(new ObjectId(productId));
  }

  public TotalStatisticResponse countByProductId(String id, Long startAt, Long endAt) {
    Criteria criteria = Criteria.where("product_id").is(id)
        .andOperator(Criteria.where("created_at").gte(startAt), Criteria.where("created_at").lte(endAt));
    AggregationOperation match = Aggregation.match(criteria);
    GroupOperation groupOperation = Aggregation.group()
        .sum("price_per_product").as("totalPrice")
        .sum("quantity").as("quantity");
    Aggregation aggregation = Aggregation.newAggregation(match, groupOperation);
    AggregationResults<TotalStatisticResponse> result = mongoTemplate.aggregate(aggregation, "product_transaction", TotalStatisticResponse.class);
    TotalStatisticResponse response = new TotalStatisticResponse();
    if (result.getUniqueMappedResult() != null) {
      response = result.getUniqueMappedResult();
    }
    return response;
  }

  public Integer countUserBuy(String username, String productId) {
    return productTransactionRepository.countByBuyerUsernameAndProductId(username, productId);
  }

  public List<ProductTransaction> findByCreatedAtBetween(Long startTimeOfDay, Long endTimeOfDay) {
    return productTransactionRepository.findByCreatedAtBetween(startTimeOfDay, endTimeOfDay);
  }

  public List<ProductTransaction> findProductTransactionByState(ProductTransactionState productTransactionState) {
    return productTransactionRepository.findProductTransactionByState(productTransactionState);
  }

  public List<ProductTransaction> findByIdIn(List<String> ids) {
    return productTransactionRepository.findByIdIn(Helper.convertListStringToListObjectId(ids));
  }
}
