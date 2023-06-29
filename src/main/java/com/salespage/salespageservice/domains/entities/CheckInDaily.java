package com.salespage.salespageservice.domains.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;

@Document("check_in_daily")
@Data
public class CheckInDaily extends BaseEntity{
    @Id
    private ObjectId id;

    @Field("date")
    private Date date;

    @Field("username")
    private String username;

    @Field("check_in")
    private Boolean checkIn = false;
}
