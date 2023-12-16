package com.salespage.salespageservice.app.controllers.AdminController;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.services.OtpService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "otp config", description = "Quản lý otp")
@RestController
@CrossOrigin
@RequestMapping("api/v1/otp")
public class OtpController extends BaseController {
  @Autowired
  private OtpService otpService;

  @GetMapping("")
  public ResponseEntity<BaseResponse> getWaitingOtp() {
    try {
      return successApi(otpService.getWaitingOtp());
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }
}
