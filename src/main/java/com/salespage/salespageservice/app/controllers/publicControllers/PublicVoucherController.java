package com.salespage.salespageservice.app.controllers.publicControllers;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.domains.services.VoucherStoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/public/voucher")
@Tag(name = "voucher", description = "mã voucher")
public class PublicVoucherController extends BaseController {

  @Autowired
  private VoucherStoreService voucherService;

  @GetMapping("{productId}")
  public ResponseEntity<?> getPublicVoucher (@PathVariable String productId) {
    try {
      return successApi(voucherService.getVoucherInProduct(productId));
    }catch (Exception e) {
      return errorApi(e);
    }
  }
}
