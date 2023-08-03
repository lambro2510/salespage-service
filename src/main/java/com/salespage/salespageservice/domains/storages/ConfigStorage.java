package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Config;
import com.salespage.salespageservice.domains.utils.CacheKey;
import com.salespage.salespageservice.domains.utils.RemoteCacheManager;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ConfigStorage extends BaseStorage {
  public Config findByKey(String key) {
    Config config = remoteCacheManager.get(CacheKey.getConfigKey(key), Config.class);
    if(Objects.isNull(config)){
      config = configRepository.findByKey(key);
      remoteCacheManager.set(CacheKey.getConfigKey(key), config, RemoteCacheManager.DAY);
    }
    return config;
  }

  public void save(Config config) {
    configRepository.save(config);
    remoteCacheManager.del(CacheKey.getConfigKey(config.getKey()));
  }

  public void deleteByKey(String key) {
    configRepository.deleteByKey(key);
  }
}
