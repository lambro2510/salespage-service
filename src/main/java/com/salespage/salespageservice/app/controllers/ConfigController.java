package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.domains.entities.Config;
import com.salespage.salespageservice.domains.services.ConfigService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/config")
public class ConfigController extends BaseController{

  @Autowired
  private ConfigService configService;

  @PutMapping("/{id}")
  public void updateConfig(@PathVariable String id, @RequestParam String value) {
    configService.updateConfig(id, value);
  }

  @PostMapping("")
  public void createConfig(@RequestBody Config config) {
    configService.createConfig(config);
  }

  @GetMapping(value = "/{id}")
  public void getConfigDetail(@PathVariable String id) {
    configService.getConfigDetail(id);
  }

  @DeleteMapping("/{id}")
  public void deleteConfig(@PathVariable String id) {
    configService.deleteConfig(id);
  }
}
