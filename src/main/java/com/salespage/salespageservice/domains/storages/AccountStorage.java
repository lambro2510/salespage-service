package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Account;
import com.salespage.salespageservice.domains.utils.CacheKey;
import org.springframework.stereotype.Component;

@Component
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
    remoteCacheManager.set(CacheKey.genSessionKey(username), token, 24 * 60 * 60);  //1 ngày
  }

  public void saveVerifyCode(String username, Integer code) {
    remoteCacheManager.set(CacheKey.getVerifyUser(username), code.toString());
  }

  public Integer getVerifyCode(String username) {
    return Integer.valueOf(remoteCacheManager.get(CacheKey.getVerifyUser(username)));
  }
}
