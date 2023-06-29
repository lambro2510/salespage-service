package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.domains.entities.CheckInDaily;
import com.salespage.salespageservice.domains.entities.CheckInDailyStatistic;
import com.salespage.salespageservice.domains.entities.User;
import com.salespage.salespageservice.domains.utils.Helper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CheckInDailyStatisticService extends BaseService{

    public void statisticUserCheckIn(){
        Date date = new Date();
        String month = Helper.getMonth(date);
        List<CheckInDaily> checkInUsers = checkInDailyStorage.findByDateAndCheckIn(date, true);
        for(CheckInDaily user : checkInUsers){
            CheckInDailyStatistic checkInDailyStatistic = checkInDailyStatisticStorage.findByUsernameAndMonth(user.getUsername(), month);
            if(Objects.isNull(checkInDailyStatistic))
                checkInDailyStatistic = new CheckInDailyStatistic();
            checkInDailyStatistic.getCheckInDayInMonth().add(date.getDay());
            checkInDailyStatisticStorage.save(checkInDailyStatistic);
        }
    }
}
