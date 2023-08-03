package com.salespage.salespageservice.domains.security.services;

import com.salespage.salespageservice.domains.entities.Account;
import com.salespage.salespageservice.domains.entities.CheckInDaily;
import com.salespage.salespageservice.domains.exceptions.WrongAccountOrPasswordException;
import com.salespage.salespageservice.domains.storages.AccountStorage;
import com.salespage.salespageservice.domains.storages.CheckInDailyStorage;
import com.salespage.salespageservice.domains.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private AccountStorage accountStorage;

  @Autowired
  private CheckInDailyStorage checkInDailyStorage;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) {
    Account account = accountStorage.findByUsername(username);
    if (account == null) {
      throw new WrongAccountOrPasswordException();
    } else {
      String today = DateUtils.nowString("dd/MM/yyyy");
      if (!today.equals(account.getLastLogin())) {
        account.setLastLogin(today);
        accountStorage.save(account);
        checkInDaily(account.getUsername());
      }
    }
    return UserDetailsImpl.build(account);
  }

  public void checkInDaily(String username) {
    String today = DateUtils.nowString("dd/MM/yyyy");
    CheckInDaily checkInDaily = checkInDailyStorage.findByUsernameAndDate(username, today);
    if (Objects.isNull(checkInDaily)) {
      checkInDaily = new CheckInDaily();
      checkInDaily.setDate(today);
      checkInDaily.setUsername(username);
      checkInDailyStorage.save(checkInDaily);
    }
  }
}

