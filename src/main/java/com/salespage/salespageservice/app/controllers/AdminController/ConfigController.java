package com.salespage.salespageservice.app.controllers.AdminController;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.app.dtos.ConfigDto;
import com.salespage.salespageservice.domains.services.ConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin config", description = "Quản lý thiết lập hệ thống")
@RestController
@CrossOrigin
@RequestMapping("api/v1/config")
public class ConfigController extends BaseController {

  @Autowired
  private ConfigService configService;

  @PutMapping("")
  public ResponseEntity<?> updateConfig(Authentication authentication, @RequestParam String key, @RequestParam String value) {
    try {
      configService.updateConfig(key, value);
      return successApi("Cập nhật  thiết lập thành công");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("")
  public ResponseEntity<?> createConfig(Authentication authentication, @RequestParam String key, @RequestParam String value) {
    try {
      configService.createConfig(new ConfigDto(key, value));
      return successApi("Tạo thiết lập thành công");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getConfigDetail(Authentication authentication, @RequestParam String key) {
    try {
      return successApi(configService.getConfigDetail(key));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @DeleteMapping("")
  public ResponseEntity<?> deleteConfig(Authentication authentication, @RequestParam String key) {
    try {
      configService.deleteConfig(key);
      return successApi("Xóa thiết lập thành công");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }
}
