package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.ProductStatistic;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductStatisticRepository extends MongoRepository<ProductStatistic, ObjectId> {

  List<ProductStatistic> findByDailyBetween(LocalDate startDate, LocalDate endDate);

  ProductStatistic findByDailyAndProductId(LocalDate daily, String productId);

  List<ProductStatistic> findByProductIdAndDailyBetween(String productId, LocalDate startDate, LocalDate endDate);

  ProductStatistic findByProductIdAndDaily(String productId, LocalDate now);

  List<ProductStatistic> findTop10ByOrderByTotalBuyDesc();

  List<ProductStatistic> findTop10ByProductIdInOrderByTotalBuyDesc(List<String> productIds);

  List<ProductStatistic> findTopNByOrderByTotalBuyDesc(int size);

  List<ProductStatistic> findTop10ByOrderByTotalViewDesc();

  List<ProductStatistic> findTop10ByProductIdInOrderByTotalViewDesc(List<String> productIds);
}
