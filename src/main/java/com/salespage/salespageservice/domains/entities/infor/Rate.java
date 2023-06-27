package com.salespage.salespageservice.domains.entities.infor;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class Rate {

    @Field("total_point")
    private float totalPoint = 0;

    @Field("total_rate")
    private float totalRate = 0;

    @Field("avg_point")
    private float avgPoint = 0;

    public void processRatePoint(Long point) {
        totalRate += 1;
        totalPoint += point;
        avgPoint = totalPoint / totalRate;
    }
}