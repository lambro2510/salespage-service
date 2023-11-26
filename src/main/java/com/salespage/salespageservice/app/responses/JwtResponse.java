package com.salespage.salespageservice.app.responses;

import com.salespage.salespageservice.domains.entities.types.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
  @Schema(description = "Tên đăng nhập của người dùng", example = "john_doe")
  private String username;

  @Schema(description = "Ảnh của người dùng", example = "john_doe")
  private String imgUrl;

  @Schema(description = "Token JWT được trả về cho người dùng đã đăng nhập", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
  private String token;

  @Schema(description = "Quyền người dungf", example = "USER")
  private UserRole role;
}
