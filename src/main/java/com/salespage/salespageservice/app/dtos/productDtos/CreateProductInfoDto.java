package com.salespage.salespageservice.app.dtos.productDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

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
  @Schema(description = "ID loại sản phẩm", example = "64c3f378baa11809a48e6cab")
  private String categoryId;

  @Size(max = 30, message = "Id cửa hàng tối đa 30 ký tự")
  @Schema(description = "Danh sách ID cửa hàng", example = "[\"642835ac24d1d851192a251d\",\"6428636624d1d851192a251e\",\"645c82f65ccca035f58f790e\"]")
  private List<String> storeIds;


}

