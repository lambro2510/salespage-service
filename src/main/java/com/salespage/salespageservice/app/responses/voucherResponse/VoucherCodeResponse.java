package com.salespage.salespageservice.app.responses.voucherResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salespage.salespageservice.domains.entities.status.VoucherCodeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class VoucherCodeResponse {

  @Schema(description = "Mã voucher")
  private String voucherCode;

  @Schema(description = "Trạng thái mã voucher")
  private VoucherCodeStatus voucherCodeStatus;

  @Schema(description = "Người đã sử dụng mã voucher")
  private String usedBy;

  @JsonFormat(pattern = "dd-MM-yyyy")
  @Schema(description = "Ngày sử dụng")
  private Date usedAt;

  @Schema(description = "Ngày hết hạn")
  private Long expireTime;
}
