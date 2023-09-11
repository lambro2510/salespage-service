package com.salespage.salespageservice.domains.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OpenStreetMapResponse {
  @JsonProperty("place_id")
  private Long placeId;

  @JsonProperty("licence")
  private String license;

  @JsonProperty("osm_type")
  private String osmType;

  @JsonProperty("osm_id")
  private Long osmId;

  @JsonProperty("lat")
  private String latitude;

  @JsonProperty("lon")
  private String longitude;

  @JsonProperty("class")
  private String classType;

  @JsonProperty("type")
  private String responseType;

  @JsonProperty("place_rank")
  private Integer placeRank;

  @JsonProperty("importance")
  private Double importance;

  @JsonProperty("addresstype")
  private String addressType;

  @JsonProperty("name")
  private String placeName;

  @JsonProperty("display_name")
  private String displayName;

  @JsonProperty("boundingbox")
  private List<String> boundingBox;

  @JsonProperty("geojson")
  private GeoJson geoJson;

  // Getter và Setter cho các trường

  @Data
  public class GeoJson {
    private String type;
    private List<List<Double>> coordinates;
  }
}
