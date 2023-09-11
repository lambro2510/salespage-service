package com.salespage.salespageservice.app.controllers.SellerControllers;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.services.PaymentStatisticService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Seller statistic", description = "Thống kê bán hàng")
@CrossOrigin
@RestController
@RequestMapping("api/v1/seller/statistic")
@SecurityRequirement(name = "bearerAuth")
public class SellerStatisticController extends BaseController {

  @Autowired private PaymentStatisticService paymentStatisticService;
  @GetMapping("")
  public ResponseEntity<BaseResponse> getTotalStatistic(Authentication authentication, @RequestParam Long lte, @RequestParam Long gte){
    try{
      return successApi(paymentStatisticService.getStatistic(gte, lte));
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @GetMapping("{productId}")
  public ResponseEntity<BaseResponse> getTotalStatisticOfProduct(Authentication authentication,
                                                                 @PathVariable String productId,
                                                                 @RequestParam Long lte,
                                                                 @RequestParam Long gte){
    try{
      return successApi(paymentStatisticService.getStatisticOfProduct(productId,gte, lte));
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }
}
