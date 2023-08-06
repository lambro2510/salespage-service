package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.BankAccount;
import com.salespage.salespageservice.domains.exceptions.BadRequestException;
import com.salespage.salespageservice.domains.info.TpBankTokenInfo;
import com.salespage.salespageservice.domains.utils.CacheKey;
import com.salespage.salespageservice.domains.utils.JsonParser;
import com.salespage.salespageservice.domains.utils.RequestUtil;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
@Log4j2
public class BankAccountStorage extends BaseStorage {

  @Value("${tp-bank.api.url}")
  private String tpBankUrl;

  @Value("${tp-bank.api.username}")
  private String tpBankUsername;

  @Value("${tp-bank.api.password}")
  private String tpBankPassword;

  @Value("${tp-bank.api.device-id}")
  private String tpBankDeviceId;

  public BankAccount findBankAccountById(String bankAccountId) {
    return bankAccountRepository.findBankAccountById(bankAccountId);
  }

  public void save(BankAccount bankAccount) {
    bankAccountRepository.save(bankAccount);
  }

  public BankAccount findByUsernameAndBankIdAndAccountNo(String username, Long bankId, String accountNumber) {
    return bankAccountRepository.findByUsernameAndBankIdAndAccountNo(username, bankId, accountNumber);
  }

  public BankAccount findByBankIdAndAccountNo(Long bankId, String accountNumber) {
    return bankAccountRepository.findByBankIdAndAccountNo(bankId, accountNumber);
  }

  public List<BankAccount> findBankAccountByIdIn(List<String> bankAccountIds) {
    return bankAccountRepository.findBankAccountByIdIn(bankAccountIds);
  }

  public String getTokenFromRemoteCache() throws Exception {
    TpBankTokenInfo tokenInfo = null;
//        remoteCacheManager.get(CacheKey.getTpBankToken(), TpBankTokenInfo.class);
//    if (Objects.isNull(tokenInfo)) {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("deviceId", tpBankDeviceId);
      jsonObject.put("username", tpBankUsername);
      jsonObject.put("password", tpBankPassword);
      jsonObject.put("step_2FA", "VERIFY");
      tokenInfo = RequestUtil.request(HttpMethod.POST, tpBankUrl + "/api/auth/login", TpBankTokenInfo.class, jsonObject, new HashMap<>());
      if (Objects.isNull(tokenInfo)) throw new BadRequestException("Lỗi khi xác nhận giao dịch");
//      remoteCacheManager.set(CacheKey.getTpBankToken(), JsonParser.toJson(tokenInfo), 60);
//    }
    log.info("tokenInfo : {{}}", tokenInfo);
    return tokenInfo.getAccess_token();
  }

  public List<BankAccount> findByUsername(String username) {
    return bankAccountRepository.findByUsername(username);
  }
}
