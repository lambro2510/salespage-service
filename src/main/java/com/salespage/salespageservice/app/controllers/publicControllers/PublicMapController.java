package com.salespage.salespageservice.app.controllers.publicControllers;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.services.MapService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/public/map")
@Tag(name = "Public map", description = "Lấy địa chỉ")
@Log4j2
public class PublicMapController extends BaseController {
  @Autowired
  private MapService mapService;

  @GetMapping("")
  public ResponseEntity<BaseResponse> getSuggestAddress(@RequestParam String address) {
    try {
      return successApi(mapService.suggestAddress(address));
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }


}
