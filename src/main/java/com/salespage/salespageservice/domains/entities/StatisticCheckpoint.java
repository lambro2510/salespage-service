package com.salespage.salespageservice.domains.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.time.LocalDate;

@Document("statistic_checkpoint")
@Data
public class StatisticCheckpoint {

  @Id
  private String id;

  @Field("check_point")
  private LocalDate checkPoint;
}
