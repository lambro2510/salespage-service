package com.salespage.salespageservice.app.dtos.productTransactionDto;

import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class ProductTransactionInfoDto {

  @NotNull(message = "Số lượng sản phẩm là bắt buộc")
  @Min(value = 0, message = "Số lượng sản phẩm tối thiểu phải là 1")
  @Max(value = 1000, message = "Số lượng sản phẩm không được lớn hơn 1000")
  @Schema(description = "Số lượng sản phẩm", example = "10")
  private Long quantity;

  @NotBlank(message = "Ghi chú không được để trống")
  @Schema(description = "Ghi chú", example = "Vui lòng gửi sản phẩm sớm nhất có thể")
  private String note;

  @NotBlank(message = "Địa chỉ nhận hàng không được để trống")
  @Schema(description = "Địa chỉ nhận hàng", example = "24/5 Phường Trường Thi")
  private String address;
}