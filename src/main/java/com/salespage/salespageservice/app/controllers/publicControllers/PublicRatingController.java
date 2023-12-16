package com.salespage.salespageservice.app.controllers.publicControllers;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.services.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/public/rating")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Rate", description = "Đánh giá")
public class PublicRatingController extends BaseController {

  @Autowired
  private RatingService ratingService;
  @GetMapping("product/{id}")
  @Operation(summary = "Những bình luận về sản phẩm", description = "Những bình luận về sản phẩm")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
      @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
      @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
  })
  public ResponseEntity<BaseResponse> getRatingProduct(@PathVariable String id, Pageable pageable) {
    try {
      return successApi(null, ratingService.getRatingOfProduct(id, pageable));
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

}
