package com.salespage.salespageservice.app.dtos.voucherDtos;

import com.salespage.salespageservice.domains.entities.status.VoucherStoreStatus;
import com.salespage.salespageservice.domains.entities.types.VoucherStoreType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UpdateVoucherStoreDto {

  @NotBlank(message = "Tên voucher store không được để trống")
  @Schema(description = "Tên voucher store", example = "Voucher Store 1")
  private String voucherStoreName;

  @NotNull(message = "Loại voucher store không được để trống")
  @Schema(description = "Loại voucher store", example = "DISCOUNT")
  private VoucherStoreType voucherStoreType;

  @Schema(description = "Trạng thái của kho voucher", example = "INACTIVE")
  private VoucherStoreStatus voucherStoreStatus;

  @Positive(message = "Giá trị không được âm")
  @Schema(description = "Giá trị của voucher", example = "5000000")
  private Long value;

  @Min(value = 0, message = "Giá trị phần trăm không được nhỏ hơn 0")
  @Max(value = 100, message = "Giá trị phần trăm không được lớn hơn 100")
  @Schema(description = "Phần trăm giá trị voucher", example = "10")
  private Long valuePercent;

  @PositiveOrZero(message = "Giá trị tối đa không được âm")
  @Schema(description = "Giá trị sản phẩm tối đa có thể áp dụng voucher", example = "100000")
  private Long maxAblePrice;

  @PositiveOrZero(message = "Giá trị tối thiểu không được âm")
  @Schema(description = "Giá trị sản phẩm tối thiểu có thể áp dụng voucher", example = "50000")
  private Long minAblePrice;

  @Positive(message = "Số voucher tối đa mà 1 user có thể nhận không được âm hoặc bằng 0")
  @Schema(description = "Số voucher tối đa mà 1 user có thể nhận", example = "1")
  private Long maxVoucherPerUser;
}