package com.salespage.salespageservice.app.dtos.productDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class ProductInfoDto extends CreateProductInfoDto {

  @Size(max = 255, message = "Url hình ảnh không quá 255 ký tự")
  @Schema(description = "Hình ảnh mặc định", example = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSmzWiqCTt0MFXE-kAQyti282xJAFWtkX86tUw775uh&s")
  private String imageUrl;

}
