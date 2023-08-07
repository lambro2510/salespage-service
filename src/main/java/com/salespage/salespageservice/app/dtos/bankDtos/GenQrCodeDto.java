package com.salespage.salespageservice.app.dtos.bankDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GenQrCodeDto {

  @NotBlank(message = "Số tài khoản là bắt buộc")
  @Schema(description = "Số tài khoản ngân hàng", required = true)
  private String accountNo;

  @NotBlank(message = "Tên tài khoản là bắt buộc")
  @Schema(description = "Tên tài khoản ngân hàng", required = true)
  private String accountName;

  @NotBlank(message = "ID acquirer là bắt buộc")
  @Schema(description = "ID acquirer", required = true)
  private String acqId;

  @NotNull(message = "Số tiền là bắt buộc")
  @Min(value = 0, message = "Số tiền phải lớn hơn hoặc bằng 0")
  @Schema(description = "Số tiền", required = true)
  private Long amount;

  @Schema(description = "Thông tin bổ sung")
  private String addInfo;

  @Schema(description = "Định dạng mã QR")
  private String format;

  @Schema(description = "Mẫu mã QR")
  private String template;
}
