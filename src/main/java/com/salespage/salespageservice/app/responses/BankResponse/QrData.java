package com.salespage.salespageservice.app.responses.BankResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class QrData {

  @Schema(description = "Mã QR")
  private String qrCode;

  @Schema(description = "URL ảnh mã QR")
  private String qrDataURL;
}
