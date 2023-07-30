package com.salespage.salespageservice.domains.entities.infor;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class Rate {

    @Field("total_point")
    private Float totalPoint = 0F;

    @Field("total_rate")
    private Float totalRate = 0F;

    @Field("avg_point")
    private Float avgPoint = 0F;

    public void processAddRatePoint(Float point) {
        totalRate += 1;
        totalPoint += point;
        avgPoint = totalPoint / totalRate;
    }

    public void processUpdateRatePoint(Float oldPoint ,Float point) {
        totalPoint = totalPoint - oldPoint + point;
        avgPoint = totalPoint / totalRate;

    }
}