package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Account;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.entities.types.UserState;
import com.salespage.salespageservice.domains.utils.CacheKey;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Log4j2
public class AccountStorage extends BaseStorage {
  public Account findByUsername(String username) {
    return accountRepository.findByUsername(username);
  }

  public boolean existByUsername(String username) {
    return accountRepository.existsByUsername(username);
  }

  public void save(Account account) {
    accountRepository.save(account);
  }

  public void saveTokenToRemoteCache(String username, String token) {
    remoteCacheManager.set(CacheKey.getUserToken(username), token, 24 * 60 * 60);  //1 ngày
  }

  public void saveVerifyCode(String username, Integer code) {
    remoteCacheManager.set(CacheKey.getVerifyUser(username), code, 600);
  }

  public Integer getVerifyCode(String username) {
    String value=  remoteCacheManager.get(CacheKey.getVerifyUser(username));
    log.info("---opt value" + value);
    if(Objects.nonNull(value)){
      return Integer.valueOf(value);
    }
    return null;
  }

  public boolean existByUsernameAndRole(String username, UserRole role) {
    return accountRepository.existsByUsernameAndRole(username, role);
  }

  public List<Account> findAll() {
    return accountRepository.findAll();
  }
}
