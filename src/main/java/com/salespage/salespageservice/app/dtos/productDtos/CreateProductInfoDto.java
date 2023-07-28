package com.salespage.salespageservice.app.dtos.productDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateProductInfoDto {

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm tối đa 255 ký tự")
    @Schema(description = "Tên sản phẩm", example = "Điện thoại iPhone 13")
    private String productName;

    @NotBlank(message = "Mô tả không được để trống")
    @Schema(description = "Mô tả sản phẩm", example = "Điện thoại iPhone 13 với thiết kế mới, camera đột phá và hiệu suất tuyệt vời")
    private String description;

    @NotNull(message = "Loại sản phẩm không được để trống")
    @Schema(description = "Loại danh mục", example = "")
    private String categoryId;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá sản phẩm phải lớn hơn 0")
    @Schema(description = "Giá sản phẩm", example = "20990000")
    private Double productPrice;

    @NotBlank(message = "Địa chỉ bán sản phẩm không được để trống")
    @Size(max = 255, message = "Địa chỉ bán sản phẩm tối đa 255 ký tự")
    @Schema(description = "Địa chỉ bán sản phẩm", example = "123 đường ABC, quận XYZ, thành phố Hồ Chí Minh")
    private String sellingAddress;

    @NotBlank(message = "Id cửa hàng không được để trống")
    @Size(max = 30, message = "Id cửa hàng tối đa 30 ký tự")
    @Schema(description = "Id cửa hàng", example = "store123")
    private String storeId;

}
