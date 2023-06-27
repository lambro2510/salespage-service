package com.salespage.salespageservice.app.dtos.storeDtos;


import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.entities.status.StoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class SellerStoreDto {
    @Schema(description = "Tên cửa hàng", example = "Cửa hàng ABC")
    private String storeName;

    @Schema(description = "Địa chỉ cửa hàng", example = "123 Đường ABC, Thành phố XYZ")
    private String address;

    @Schema(description = "Mô tả cửa hàng", example = "Cửa hàng chuyên bán đồ điện tử")
    private String description;

    @Schema(description = "Trạng thái cửa hàng", example = "ACTIVE")
    private StoreStatus status;

    @Schema(description = "Danh sách sản phẩm đang bán", example = "[\"LAPTOP\",\"PHONE\"]")
    private List<String> sellProducts;

    public void assignFromSellerStoreDto(SellerStore sellerStore) {
        storeName = sellerStore.getStoreName();
        address = sellerStore.getAddress();
        description = sellerStore.getDescription();
        status = sellerStore.getStatus();
        sellProducts = sellerStore.getSellProducts();
    }
}
