package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document("statistic_checkpoint")
@Data
public class StatisticCheckpoint {

  @Id
  private String id;

  @Field("check_point")
  private LocalDate checkPoint;
}
