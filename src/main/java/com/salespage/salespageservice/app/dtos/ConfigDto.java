package com.salespage.salespageservice.app.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigDto {

  @NotBlank(message = "Khóa (key) là bắt buộc")
  @Schema(description = "Khóa (key) của cấu hình", required = true)
  private String key;

  @NotBlank(message = "Giá trị (value) là bắt buộc")
  @Size(max = 500, message = "Giá trị (value) không vượt quá 500 ký tự")
  @Schema(description = "Giá trị (value) của cấu hình", required = true, maxLength = 500)
  private String value;
}
