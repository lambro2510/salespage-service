package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Config;
import com.salespage.salespageservice.domains.utils.CacheKey;
import com.salespage.salespageservice.domains.utils.RemoteCacheManager;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ConfigStorage extends BaseStorage {
  public Config findById(String id) {
    Config config = remoteCacheManager.get(CacheKey.getConfigKey(id), Config.class);
    if(Objects.isNull(config)){
      config = configRepository.findById(id).get();
      remoteCacheManager.set(CacheKey.getConfigKey(id), config, RemoteCacheManager.DAY);
    }
    return config;
  }

  public void save(Config config) {
    configRepository.save(config);
    remoteCacheManager.del(CacheKey.getConfigKey(config.getKey()));
  }

  public void deleteById(String id) {
    configRepository.deleteById(id);
  }
}
