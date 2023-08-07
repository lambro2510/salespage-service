package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.ConfigDto;
import com.salespage.salespageservice.domains.entities.Config;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ConfigService extends BaseService {

  public void createConfig(ConfigDto dto) {
    Config config = new Config();
    config.setValue(dto.getValue());
    config.setKey(dto.getKey());
    configStorage.save(config);
  }

  public Config getConfigDetail(String key) {
    return configStorage.findByKey(key);
  }

  public void updateConfig(String key, String value) {
    Config config = configStorage.findByKey(key);
    if (Objects.isNull(configStorage)) throw new ResourceNotFoundException("Không tồn tại config này");
    config.setValue(value);
    configStorage.save(config);
  }

  public void deleteConfig(String key) {
    configStorage.deleteByKey(key);
  }
}
