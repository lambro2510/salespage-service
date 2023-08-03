package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.Config;
import com.salespage.salespageservice.domains.utils.RemoteCacheManager;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends MongoRepository<Config, String> {
  Config findByKey(String key);

  void deleteByKey(String key);
}
