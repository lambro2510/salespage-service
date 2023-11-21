package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.ProductStatistic;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductStatisticRepository extends MongoRepository<ProductStatistic, ObjectId> {

  List<ProductStatistic> findByDailyBetween(LocalDate startDate, LocalDate endDate);

  ProductStatistic findByDailyAndProductId(LocalDate daily, String productId);

  List<ProductStatistic> findByProductDetailIdAndDailyBetween(String productId, LocalDate startDate, LocalDate endDate);

  ProductStatistic findFirstByProductIdAndDailyBetweenOrderByTotalViewAsc(String productId, LocalDate start, LocalDate end);
  List<ProductStatistic> findByProductIdAndDailyBetweenOrderByTotalViewAsc(String productId, LocalDate start, LocalDate end);

  List<ProductStatistic> findTop12ByOrderByTotalBuyDesc();

  List<ProductStatistic> findTop12ByProductIdInOrderByTotalBuyDesc(List<String> productIds);

  List<ProductStatistic> findTopNByOrderByTotalBuyDesc(int size);

  List<ProductStatistic> findTop12ByProductIdInOrderByTotalViewDesc(List<String> productIds);

  HashSet<String> findDistinctTop10ProductIdByProductIdInOrderByTotalViewDesc(List<String> productIds);

  HashSet<ProductStatistic> findDistinctTop10ProductIdByOrderByTotalViewDesc();

  List<ProductStatistic> findTop12ByOrderByTotalViewDesc();
  List<ProductStatistic> findByProductIdIn(List<String> ids);

  ProductStatistic findByDailyAndProductDetailId(LocalDate current, String id);

  ProductStatistic findFirstByProductId(String productId);

  ProductStatistic findFirstByProductIdAndDailyOrderByTotalViewAsc(String productId, LocalDate now);
}
