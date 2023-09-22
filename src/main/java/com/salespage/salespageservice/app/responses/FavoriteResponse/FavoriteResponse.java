package com.salespage.salespageservice.app.responses.FavoriteResponse;

import com.salespage.salespageservice.domains.entities.types.FavoriteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponse {
  FavoriteType type;

  String refId;

  String name;
}
