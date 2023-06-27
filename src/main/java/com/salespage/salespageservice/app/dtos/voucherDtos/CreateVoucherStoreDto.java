package com.salespage.salespageservice.app.dtos.voucherDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateVoucherStoreDto extends UpdateVoucherStoreDto {

    @NotBlank(message = "ID sản phẩm không được để trống")
    @Schema(description = "ID sản phẩm", example = "123456")
    private String productId;
}
