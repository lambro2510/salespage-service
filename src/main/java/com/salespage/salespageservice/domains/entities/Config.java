package com.salespage.salespageservice.domains.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document("config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Config {

  @Id
  private ObjectId id;

  @Field("key")
  private String key;

  @Field("value")
  private String value;
}
