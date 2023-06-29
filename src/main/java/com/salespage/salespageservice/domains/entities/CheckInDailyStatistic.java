package com.salespage.salespageservice.domains.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Document("check_in_daily_statistic")
@Data
public class CheckInDailyStatistic {
    @Id
    private ObjectId id;

    @Field("username")
    private String username;

    @Field("month")
    private String month;

    @Field("check_in_day")
    private List<Integer> checkInDayInMonth = new ArrayList<>();
}
