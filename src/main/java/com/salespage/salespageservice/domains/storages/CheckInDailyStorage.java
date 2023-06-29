package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.CheckInDaily;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CheckInDailyStorage extends BaseStorage{
    public void save(CheckInDaily checkInDaily) {
        checkInDailyRepository.save(checkInDaily);
    }

    public CheckInDaily findByUsernameAndDate(String username, Date today) {
        return checkInDailyRepository.findByUsernameAndDate(username, today);
    }
}
