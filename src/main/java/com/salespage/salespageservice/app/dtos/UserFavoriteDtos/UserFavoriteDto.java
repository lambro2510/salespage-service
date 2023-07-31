package com.salespage.salespageservice.app.dtos.UserFavoriteDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salespage.salespageservice.domains.entities.types.FavoriteType;
import lombok.Data;

@Data
public class UserFavoriteDto {
    String refId;

    FavoriteType favoriteType;

    @JsonProperty("isLike")
    Boolean isLike;
}
