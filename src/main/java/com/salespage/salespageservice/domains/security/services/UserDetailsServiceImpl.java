package com.salespage.salespageservice.domains.security.services;

import com.salespage.salespageservice.app.dtos.accountDtos.CheckInDto;
import com.salespage.salespageservice.domains.entities.Account;
import com.salespage.salespageservice.domains.exceptions.WrongAccountOrPasswordException;
import com.salespage.salespageservice.domains.producer.Producer;
import com.salespage.salespageservice.domains.storages.AccountStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private AccountStorage accountStorage;

  @Autowired
  private Producer producer;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) {
    Account account = accountStorage.findByUsername(username);
    if (account == null) {
      throw new WrongAccountOrPasswordException();
    }
    return UserDetailsImpl.build(account);
  }

  public void checkIn(CheckInDto dto) {
    producer.checkIn(dto);

  }
}

