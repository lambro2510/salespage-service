package com.salespage.salespageservice.app.dtos.accountDtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

@Data
public class SignUpDto {

    @NotBlank(message = "Tên tài khoản là bắt buộc")
    @Size(min = 8, max = 18, message = "Tên tài khoản phải từ 8 đến 18 ký tự")
    @Schema(description = "Tên tài khoản của người dùng")
    private String username;

    @NotBlank(message = "Mật khẩu là bắt buộc")
    @Size(min = 6, max = 24, message = "Mật khẩu phải từ 6 đến 24 ký tự")
    @Schema(description = "Mật khẩu của người dùng")
    private String password;

    @NotBlank(message = "Xác nhận mật khẩu là bắt buộc")
    @Size(min = 6, max = 24, message = "Xác nhận mật khẩu phải từ 6 đến 24 ký tự")
    @Schema(description = "Xác nhận lại mật khẩu của người dùng")
    private String confirmPassword;

    @NotBlank(message = "Tên là bắt buộc")
    @Schema(description = "Tên của người dùng")
    private String firstName;

    @NotBlank(message = "Họ là bắt buộc")
    @Schema(description = "Họ của người dùng")
    private String lastName;

    @NotBlank(message = "Email là bắt buộc")
    @Email(message = "Email phải là địa chỉ email hợp lệ")
    @Schema(description = "Email của người dùng")
    private String email;

    @NotBlank(message = "Số điện thoại là bắt buộc")
    @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Số điện thoại phải là số điện thoại quốc tế hợp lệ")
    @Schema(description = "Số điện thoại của người dùng")
    private String phoneNumber;

    @NotNull(message = "Ngày sinh là bắt buộc")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Past(message = "Ngày sinh không được lớn hơn ngày hiện tại")
    @Schema(description = "Ngày sinh của người dùng", format = "date", example = "01-01-1990")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dateOfBirth;

    @Schema(description = "Quyền của người dùng", example = "USER")
    private UserRole userRole;

}
