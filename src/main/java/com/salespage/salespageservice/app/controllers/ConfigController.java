package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.ConfigDto;
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

  @PutMapping("")
  public ResponseEntity<?> updateConfig(@RequestParam String key, @RequestParam String value) {
    try{
      configService.updateConfig(key, value);
      return successApi("Cập nhật  thiết lập thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("")
  public ResponseEntity<?> createConfig(@RequestBody ConfigDto config) {
    try{
      configService.createConfig(config);
      return successApi("Tạo thiết lập thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getConfigDetail(@RequestParam String key) {
    try{
      return successApi(configService.getConfigDetail(key));
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @DeleteMapping("")
  public ResponseEntity<?> deleteConfig(@RequestParam String key) {
    try{
      configService.deleteConfig(key);
      return successApi("Xóa thiết lập thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }
}
