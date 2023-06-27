package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.domains.entities.types.LogType;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document("system_log")
@Data
public class SystemLog {
    @Id
    private ObjectId id;

    @Field("log_type")
    private LogType logType;

    @Field("message")
    private String message;

    @Field("trace")
    private String trace;

    @Field("created_by")
    private String createdBy;

    @Field("created_at")
    private LocalDateTime createdAt;
}
