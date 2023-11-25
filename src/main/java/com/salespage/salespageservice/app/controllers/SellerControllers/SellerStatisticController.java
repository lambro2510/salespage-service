package com.salespage.salespageservice.app.controllers.SellerControllers;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.services.StatisticService;
import com.salespage.salespageservice.domains.utils.DateUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Tag(name = "Seller statistic", description = "Thống kê bán hàng")
@CrossOrigin
@RestController
@RequestMapping("api/v1/seller/statistic")
@SecurityRequirement(name = "bearerAuth")
public class SellerStatisticController extends BaseController {

  @Autowired private StatisticService statisticService;
  @GetMapping("")
  public ResponseEntity<BaseResponse> getTotalStatistic(Authentication authentication, @RequestParam Long lte, @RequestParam Long gte){
    try{
      return successApi(statisticService.getStatistic(gte, lte));
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
      LocalDate startDate = DateUtils.convertLongToLocalDate(gte);
      LocalDate endDate = DateUtils.convertLongToLocalDate(lte);
      return successApi(statisticService.getStatisticOfProduct(productId,startDate, endDate));
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }
}
