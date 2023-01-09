package com.salespage.salespageservice.domains.security.services;

import com.salespage.salespageservice.domains.entities.Account;
import com.salespage.salespageservice.domains.exceptions.WrongAccountOrPasswordException;
import com.salespage.salespageservice.domains.storages.AccountStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private AccountStorage accountStorage;

  @Override
  public UserDetails loadUserByUsername(String username) {
    Account account = accountStorage.findByUsername(username);
    if (account == null) {
      throw new WrongAccountOrPasswordException();
    }
    return UserDetailsImpl.build(account);
  }
}
