package com.salespage.salespageservice.app.dtos.bankDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Oath2CassoDto {

  @NotBlank(message = "Client ID là bắt buộc")
  @JsonProperty("client_id")
  @Schema(description = "Client ID", required = true)
  private String clientId;

  @Schema(description = "Phạm vi (scope)")
  private String scope;

  @NotBlank(message = "URL chuyển hướng là bắt buộc")
  @JsonProperty("redirect_uri")
  @Schema(description = "URL chuyển hướng", required = true)
  private String redirectUri;

  @NotBlank(message = "Loại phản hồi là bắt buộc")
  @JsonProperty("response_type")
  @Schema(description = "Loại phản hồi", required = true)
  private String responseType;

  @Schema(description = "Trạng thái (state)")
  private String state;
}
