package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.app.dtos.bankDtos.Oath2CassoDto;
import com.salespage.salespageservice.domains.entities.BankTransaction;
import com.salespage.salespageservice.domains.utils.CacheKey;
import com.salespage.salespageservice.domains.utils.RequestUtil;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class BankTransactionStorage extends BaseStorage{
  @Value("${casso.url}")
  String cassoUrl;

  @Value("${casso.bank-acc-id}")
  String bankAccId;
  public void saveAll(List<BankTransaction> bankTransactions) {
    bankTransactionRepository.saveAll(bankTransactions);
  }

  public List<BankTransaction> findAll() {
    return bankTransactionRepository.findAll();
  }

  public String getOath2Token(String clientId) {
    String oath2Key = remoteCacheManager.get(CacheKey.getOath2Key(clientId));
    if(oath2Key == null){
      Oath2CassoDto oath2CassoDto = new Oath2CassoDto();
      oath2CassoDto.setClientId(clientId);
      oath2CassoDto.setScope("webhook%20transaction");
      oath2CassoDto.setRedirectUri("https://www.example.com/auth-callback");
      oath2CassoDto.setResponseType("code");
      oath2CassoDto.setState("1");
      oath2Key = (String) RequestUtil.request(HttpMethod.POST, cassoUrl + "/auth/authorize",oath2CassoDto,new HashMap<>());
      remoteCacheManager.set(CacheKey.getOath2Key(clientId), oath2Key, 3600 * 5); //5h do 6h token hết hạn
    }
    return oath2Key;
  }

  public BankTransaction findByDescription(String description) {
    return bankTransactionRepository.findByDescriptionLike(description);
  }

  public List<BankTransaction> findByDescriptionIn(List<String> description) {
    return bankTransactionRepository.findByDescriptionLikeIn(description);
  }
}
