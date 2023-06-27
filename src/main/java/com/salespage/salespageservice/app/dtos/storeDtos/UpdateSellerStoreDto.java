package com.salespage.salespageservice.app.dtos.storeDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateSellerStoreDto extends SellerStoreDto {
    @Schema(description = "Id cửa hàng", example = "645c82f65ccca035f58f790e")
    private String storeId;


}
