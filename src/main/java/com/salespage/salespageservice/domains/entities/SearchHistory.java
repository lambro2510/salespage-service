package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.domains.entities.types.SearchType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Document("search_history")
@Data
public class SearchHistory extends BaseEntity{
  @Id
  private ObjectId id;

  @Field("username")
  private String username;

  @Field("search_data")
  private String searchData;

  @Field("search_type")
  private SearchType searchType;

}
