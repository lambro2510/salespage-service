package com.salespage.salespageservice.app.controllers.AdminController;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.domains.entities.status.ShipperStatus;
import com.salespage.salespageservice.domains.services.ShipperService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin shipper", description = "Quản lý người giao hàng")
@RestController
@CrossOrigin
@RequestMapping("api/v1/admin/config")
public class AdminShipperController extends BaseController {

  @Autowired
  private ShipperService shipperService;

  @GetMapping("")
  public ResponseEntity<?> getAllShipper(Authentication authentication, Pageable pageable) {
    try {

      return successApi(shipperService.getAllShipper(getUsername(authentication), pageable));
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

  @PostMapping("")
  public ResponseEntity<?> createShipperUser(Authentication authentication, @RequestParam String username) {
    try {
      shipperService.createShipperUser(getUsername(authentication), username);
      return successApi("Xác minh người giao hàng thành công");
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

  @PutMapping("")
  public ResponseEntity<?> updateShipperUser(Authentication authentication, @RequestParam String username, @RequestParam ShipperStatus status) {
    try {
      shipperService.updateShipper(getUsername(authentication), username, status);
      return successApi("Xác minh người giao hàng thành công");
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }
}
