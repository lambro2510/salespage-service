package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Otp;
import com.salespage.salespageservice.domains.utils.CacheKey;
import org.springframework.stereotype.Component;

@Component
public class OtpStorage extends BaseStorage{
  public void saveVerifyCode(String username, Otp otp) {
    otpRepository.save(otp);
    remoteCacheManager.set(CacheKey.getVerifyUser(username), otp, 600);
  }
}
