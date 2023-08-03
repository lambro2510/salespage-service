package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.domains.entities.Config;
import com.salespage.salespageservice.domains.services.ConfigService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/config")
public class ConfigController extends BaseController{

  @Autowired
  private ConfigService configService;

  @PutMapping("/{id}")
  public ResponseEntity<?> updateConfig(@PathVariable String id, @RequestParam String value) {
    try{
      configService.updateConfig(id, value);
      return successApi("Cập nhật  thiết lập thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("")
  public ResponseEntity<?> createConfig(@RequestBody Config config) {
    try{
      configService.createConfig(config);
      return successApi("Tạo thiết lập thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getConfigDetail(@PathVariable String id) {
    try{
      return successApi(configService.getConfigDetail(id));
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteConfig(@PathVariable String id) {
    configService.deleteConfig(id);
    try{
      configService.deleteConfig(id);
      return successApi("Xóa thiết lập thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }
}
