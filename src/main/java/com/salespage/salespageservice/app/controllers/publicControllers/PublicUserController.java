package com.salespage.salespageservice.app.controllers.publicControllers;

import com.salespage.salespageservice.domains.entities.User;
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
@RequestMapping("v1/api/public/user")
@Tag(name = "Thông tin người dùng", description = "Thông tin của người dùng tài khoản")
public class PublicUserController {

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
  public ResponseEntity<User> getUserDetail(@Parameter(description = "Tên đăng nhập của người dùng cần lấy thông tin") @RequestParam String username) {
    return userService.getUserDetail(username);
  }
}
