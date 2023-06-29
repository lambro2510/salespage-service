package com.salespage.salespageservice.domains.security.services;

import com.salespage.salespageservice.domains.entities.Account;
import com.salespage.salespageservice.domains.entities.CheckInDaily;
import com.salespage.salespageservice.domains.exceptions.WrongAccountOrPasswordException;
import com.salespage.salespageservice.domains.storages.AccountStorage;
import com.salespage.salespageservice.domains.storages.CheckInDailyStorage;
import com.salespage.salespageservice.domains.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountStorage accountStorage;

    @Autowired
    private CheckInDailyStorage checkInDailyStorage;
    @Override
    public UserDetails loadUserByUsername(String username) {
        Account account = accountStorage.findByUsername(username);
        if (account == null) {
            throw new WrongAccountOrPasswordException();
        }
        return UserDetailsImpl.build(account);
    }

    public void checkInDaily(String username){
        Date today = new Date();
        CheckInDaily checkInDaily = checkInDailyStorage.findByUsernameAndDate(username, Helper.getDay(today));
        if(Objects.isNull(checkInDaily)){
            checkInDaily = new CheckInDaily();
            checkInDaily.setDate(Helper.getDay(today));
            checkInDaily.setUsername(username);
            checkInDailyStorage.save(checkInDaily);
        }
    }
}

