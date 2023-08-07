package com.salespage.salespageservice.app.responses.voucherResponse;

import com.salespage.salespageservice.domains.entities.status.VoucherStoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ListVoucherCodeResponse {

  @Schema(description = "Danh sách mã voucher")
  List<VoucherCodeResponse> voucherCodes = new ArrayList<>();

  @Schema(description = "ID cửa hàng voucher")
  private String voucherStoreId;

  @Schema(description = "Tên cửa hàng voucher")
  private String voucherStoreName;

  @Schema(description = "Trạng thái cửa hàng voucher")
  private VoucherStoreStatus voucherStoreStatus;
}
