package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.userDtos.UserInfoDto;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("api/v1/user")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User", description = "API quản lý thông tin và hồ sơ người dùng")
public class UserController extends BaseController {

  @Autowired
  private UserService userService;

  @GetMapping("profile")
  @Operation(summary = "Lấy thông tin hồ sơ người dùng", description = "Lấy thông tin hồ sơ cho người dùng đã xác thực")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
      @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
      @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
  })
  public ResponseEntity<BaseResponse> getProfile(Authentication authentication) {
    try {
      return successApi(null, userService.getUserDetail(getUsername(authentication)));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }


  @PutMapping("")
  @Operation(summary = "Cập nhật thông tin người dùng", description = "Cập nhật thông tin hồ sơ cho người dùng đã xác thực")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
      @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
      @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
  })
  public ResponseEntity<BaseResponse> updateUser(Authentication authentication, @RequestBody @Schema(description = "Thông tin người dùng cần cập nhật") UserInfoDto dto) {
    try {
      return successApi("Cập nhật thông tin người dùng thành công", userService.updateUser(getUsername(authentication), dto));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("uploadImage")
  @Operation(summary = "Upload user profile image", description = "Upload a new profile image for the authenticated user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Image upload successful"),
      @ApiResponse(responseCode = "400", description = "Invalid file format"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<BaseResponse> uploadImage(Authentication authentication, @RequestBody @Schema(type = "multipart", format = "binary") MultipartFile file) throws IOException {
    try {
      userService.uploadImage(getUsername(authentication), file);
      return successApi("Tải ảnh người dùng lên thành công");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("voting")
  @Operation(summary = "Vote for a user", description = "Vote for another user by their user ID and the number of points to give")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Vote successful"),
      @ApiResponse(responseCode = "400", description = "Invalid user ID or points"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<BaseResponse> voting(Authentication authentication, @Parameter(description = "ID of the user to vote for") @RequestParam String userId, @Parameter(description = "Number of points to give") @RequestParam Float point) {
    try {

      return successApi("Đánh giá người dùng thành công", userService.voting(getUsername(authentication), userId, point));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }


}
