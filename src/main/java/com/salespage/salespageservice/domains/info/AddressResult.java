package com.salespage.salespageservice.domains.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AddressResult {
  private List<Result> results;
  private String status;

  @Data
  public static class Result {
    @JsonProperty("address_components")
    private List<AddressComponent> addressComponents;
    @JsonProperty("formatted_address")
    private String formattedAddress;
    private Geometry geometry;
    private String place_id;
    private String reference;
    private PlusCode plus_code;
    private Compound compound;
    private List<String> types;
    private String name;
    private String address;

    @Data
    public static class AddressComponent {
      private String long_name;
      private String short_name;
    }

    @Data
    public static class Geometry {
      private Location location;
      private Object boundary; // You can specify a more specific type if needed

      @Data
      public static class Location {
        private double lat;
        private double lng;
      }
    }

    @Data
    public static class PlusCode {
      private String compound_code;
      private String global_code;
    }

    @Data
    public static class Compound {
      private String district;
      private String commune;
      private String province;
    }
  }
}
