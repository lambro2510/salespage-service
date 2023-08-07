package com.salespage.salespageservice.app.dtos.UserFavoriteDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salespage.salespageservice.domains.entities.types.FavoriteType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserFavoriteDto {

  @NotNull(message = "ID tham chiếu là bắt buộc")
  @Schema(description = "ID tham chiếu", required = true)
  private String refId;

  @NotNull(message = "Loại yêu thích là bắt buộc")
  @Schema(description = "Loại yêu thích", required = true)
  private FavoriteType favoriteType;

  @JsonProperty("isLike")
  @Schema(description = "Trạng thái yêu thích (like/dislike)", required = true)
  private Boolean isLike;
}
