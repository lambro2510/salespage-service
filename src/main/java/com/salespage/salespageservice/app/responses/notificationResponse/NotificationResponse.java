package com.salespage.salespageservice.app.responses.notificationResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salespage.salespageservice.domains.entities.status.NotificationStatus;
import lombok.Data;

import java.util.Date;

@Data
public class NotificationResponse {

    private String id;

    private String tittle;

    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date created;

    private NotificationStatus status;
}
