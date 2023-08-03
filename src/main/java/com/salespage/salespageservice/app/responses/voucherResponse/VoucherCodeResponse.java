package com.salespage.salespageservice.app.responses.voucherResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salespage.salespageservice.domains.entities.status.VoucherCodeStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class VoucherCodeResponse {
  private String voucherCode;

  private VoucherCodeStatus voucherCodeStatus;

  private String usedBy;

  @JsonFormat(pattern = "dd-MM-yyyy")
  private Date usedAt;

  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate expireTime;
}
