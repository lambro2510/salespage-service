package com.salespage.salespageservice.domains.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LocationResult {
  private List<Prediction> predictions;

  @JsonProperty("execution_time")
  private String executionTime;

  private String status;

  @Data
  public static class Prediction {
    private String description;

    @JsonProperty("matched_substrings")
    private List<MatchedSubstring> matchedSubstrings;

    @JsonProperty("place_id")
    private String placeId;

    private String reference;

    @JsonProperty("structured_formatting")
    private StructuredFormatting structuredFormatting;

    @JsonProperty("has_children")
    private boolean hasChildren;

    @JsonProperty("plus_code")
    private PlusCode plusCode;

    private Compound compound;

    private List<Term> terms;

    private List<String> types;

    @JsonProperty("distance_meters")
    private Double distanceMeters;

    // Getter and setter methods for all fields
  }

  public static class MatchedSubstring {
    private int length;
    private int offset;

    // Getter and setter methods for length and offset
  }

  public static class StructuredFormatting {
    @JsonProperty("main_text")
    private String mainText;

    @JsonProperty("main_text_matched_substrings")
    private List<MatchedSubstring> mainTextMatchedSubstrings;

    @JsonProperty("secondary_text")
    private String secondaryText;

    @JsonProperty("secondary_text_matched_substrings")
    private List<MatchedSubstring> secondaryTextMatchedSubstrings;

    // Getter and setter methods for all fields
  }

  public static class PlusCode {
    @JsonProperty("compound_code")
    private String compoundCode;

    @JsonProperty("global_code")
    private String globalCode;

  }

  public static class Compound {
    private String district;
    private String commune;
    private String province;

  }

  public static class Term {
    private int offset;
    private String value;

  }
}


