package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.accountDtos.LoginDto;
import com.salespage.salespageservice.app.dtos.accountDtos.SignUpDto;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("api/v1/account")
@Tag(name = "Account", description = "Quản lý tài khoản của người dùng")
public class AccountController extends BaseController {

  @Autowired
  private AccountService accountService;

  @PostMapping("sign-up")
  @Operation(summary = "Dăng ký tài khoản", description = "Đăng ký tài khoản mới")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tạo tài khoản thành công"),
      @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ, vui lòng kiểm tra nội dung yêu cầu"),
      @ApiResponse(responseCode = "409", description = "Tài khoản với email/tên đăng nhập này đã tồn tại"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> signUp(@RequestBody @Valid SignUpDto dto) {
    try {
      return successApi("Đăng ký thành công", accountService.signUp(dto));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("sign-up/admin")
  @Operation(summary = "Tạo tài khoản admin", description = "Tạo tài khoản admin")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tạo tài khoản thành công"),
      @ApiResponse(responseCode = "409", description = "Tài khoản đã tồn tại"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> createdAdminRole() {
    try {
      return successApi("Tạo tài khoản thành công", accountService.createdAdminRole());
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("sign-in")
  @Operation(summary = "Đăng nhập", description = "Đăng nhập tài khoản")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Xác thực tài khoản thành công"),
      @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ, vui lòng kiểm tra nội dung yêu cầu"),
      @ApiResponse(responseCode = "401", description = "Không được ủy quyền, vui lòng kiểm tra thông tin đăng nhập của bạn"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> login(@RequestBody @Valid LoginDto dto) throws IOException {
    try {
      return successApi("Đăng nhập thành công", accountService.signIn(dto));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("verify-code")
  @Operation(summary = "Tạo mã xác nhân", description = "Tạo mã xác nhận")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tạo mã xác minh thành công"),
      @ApiResponse(responseCode = "401", description = "Không được ủy quyền, vui lòng kiểm tra thông tin xác thực của bạn"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<BaseResponse> createVerifyCode(Authentication authentication) {
    try {
      accountService.createVerifyCode(getUsername(authentication));
      return successApi("Tạo mã xác nhận thành công");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("verify")
  @Operation(summary = "Xác nhận mã đăng nhập", description = "Xác nhận mã đăng nhâp")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Xác minh mã xác minh thành công"),
      @ApiResponse(responseCode = "401", description = "Không được ủy quyền, vui lòng kiểm tra thông tin xác thực của bạn"),
      @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ, vui lòng kiểm tra các thông số yêu cầu"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> verifyCode(@RequestParam("code") Integer code, String username) {
    try {
      accountService.verifyCode(username, code);
      return successApi("Xác minh thành công");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }


}