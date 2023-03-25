package com.salespage.salespageservice.domains.repositories.base.impl;


import com.salespage.salespageservice.domains.repositories.base.MongoResourceRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

@Log4j2
public class MongoResourceRepositoryImpl<T, I extends Serializable>
    extends SimpleMongoRepository<T, I> implements MongoResourceRepository<T, I> {
  private final MongoOperations mongoOperations;
  private final MongoEntityInformation entityInformation;

  public MongoResourceRepositoryImpl(
      final MongoEntityInformation entityInformation, final MongoOperations mongoOperations) {
    super(entityInformation, mongoOperations);

    this.entityInformation = entityInformation;
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Page<T> findAll(final Query query, final Pageable pageable) {
    Assert.notNull(query, "Query must not be null!");
    Assert.notNull(pageable, "Pageable must not be null!");

    long totalPage =
        mongoOperations.count(
            query, entityInformation.getJavaType(), entityInformation.getCollectionName());

    List<T> list =
        mongoOperations.find(
            query.with(pageable),
            entityInformation.getJavaType(),
            entityInformation.getCollectionName());

    return PageableExecutionUtils.getPage(list, pageable, () -> totalPage);
  }

  @Override
  public List<T> findAll(Query query) {
    return mongoOperations.find(
        query, entityInformation.getJavaType(), entityInformation.getCollectionName());
  }
}
