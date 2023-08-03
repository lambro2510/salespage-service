package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.domains.entities.Config;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ConfigService extends BaseService {

  public void createConfig(Config config) {
    configStorage.save(config);
  }

  public Config getConfigDetail(String id) {
    return configStorage.findById(id);
  }

  public void updateConfig(String id, String value) {
    Config config =  configStorage.findById(id);
    if(Objects.isNull(configStorage)) throw new ResourceNotFoundException("Không tồn tại config này");
    config.setValue(value);
    configStorage.save(config);
  }

  public void deleteConfig(String id) {
    configStorage.deleteById(id);
  }
}
