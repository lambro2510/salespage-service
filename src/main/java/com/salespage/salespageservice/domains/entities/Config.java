package com.salespage.salespageservice.domains.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document("config")
@Data
public class Config {

  @Id
  private ObjectId id;

  @Field("key")
  private String key;

  @Field("value")
  private String value;
}
