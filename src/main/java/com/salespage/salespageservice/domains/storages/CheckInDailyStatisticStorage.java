package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.CheckInDailyStatistic;
import com.salespage.salespageservice.domains.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CheckInDailyStatisticStorage extends BaseStorage{

    public CheckInDailyStatistic findByUsernameAndMonth(String username, String month) {
        return checkInDailyStatisticRepository.findByUsernameAndMonth(username, month);
    }

    public void save(CheckInDailyStatistic checkInDailyStatistic) {
        checkInDailyStatisticRepository.save(checkInDailyStatistic);
    }
}
