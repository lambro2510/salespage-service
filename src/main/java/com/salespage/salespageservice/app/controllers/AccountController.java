import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("v1/api/account")
public class AccountController extends BaseController {

  // ... other code

  @ApiOperation(value = "Đăng ký tài khoản", notes = "Đăng ký tài khoản mới")
  @ApiResponses(value = {
          @ApiResponse(code = 201, message = "Đăng ký thành công"),
          @ApiResponse(code = 400, message = "Dữ liệu đầu vào không hợp lệ"),
          @ApiResponse(code = 409, message = "Tài khoản đã tồn tại")
  })
  @PostMapping("sign-up")
  public ResponseEntity<JwtResponse> signUp(@RequestBody @Valid SignUpDto dto) {
    return accountService.signUp(dto);
  }

  @ApiOperation(value = "Đăng nhập", notes = "Đăng nhập vào tài khoản đã đăng ký")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Đăng nhập thành công"),
          @ApiResponse(code = 400, message = "Dữ liệu đầu vào không hợp lệ"),
          @ApiResponse(code = 401, message = "Đăng nhập thất bại")
  })
  @PostMapping("sign-in")
  public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginDto dto) throws IOException {
    return accountService.signIn(dto);
  }

  @ApiOperation(value = "Tạo mã xác nhận", notes = "Tạo mã xác nhận cho tài khoản")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Tạo mã xác nhận thành công"),
          @ApiResponse(code = 401, message = "Bạn không có quyền truy cập")
  })
  @PostMapping("verify-code")
  public ResponseEntity<String> createVerifyCode(Authentication authentication) {
    return accountService.createVerifyCode(getUsername(authentication));
  }

  @ApiOperation(value = "Xác nhận tài khoản", notes = "Xác nhận tài khoản với mã xác nhận đã tạo")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Xác nhận tài khoản thành công"),
          @ApiResponse(code = 401, message = "Bạn không có quyền truy cập"),
          @ApiResponse(code = 404, message = "Tài khoản không tồn tại"),
          @ApiResponse(code = 409, message = "Mã xác nhận không chính xác")
  })
  @PostMapping("verify")
  public ResponseEntity<String> verifyCode(Authentication authentication,
                                            @ApiParam(value = "Mã xác nhận", required = true)
                                            @RequestParam Integer code) {
    return accountService.verifyCode(getUsername(authentication), code);
  }
}
