package com.salespage.salespageservice.app.controllers.publicControllers;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/public/user")
@Tag(name = "User", description = "Thông tin của người dùng tài khoản")
public class PublicUserController extends BaseController {

  @Autowired
  private UserService userService;

  @GetMapping("detail")
  @Operation(summary = "Lấy thông tin chi tiết người dùng", description = "Lấy thông tin chi tiết cho một người dùng cụ thể theo tên đăng nhập")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
      @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
      @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
  })
  public ResponseEntity<BaseResponse> getUserDetail(@Parameter(description = "Tên đăng nhập của người dùng cần lấy thông tin") @RequestParam String username) {
    try {
      return successApi(null, userService.getUserDetail(username));
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }
}
