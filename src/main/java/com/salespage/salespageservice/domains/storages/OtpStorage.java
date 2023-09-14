package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Otp;
import com.salespage.salespageservice.domains.entities.types.OtpStatus;
import com.salespage.salespageservice.domains.utils.CacheKey;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OtpStorage extends BaseStorage{
  public void saveVerifyCode(String username, Otp otp) {
    otpRepository.save(otp);
    remoteCacheManager.set(CacheKey.getVerifyUser(username), otp, 600);
  }

  public List<Otp> findByStatus(OtpStatus otpStatus) {
    return otpRepository.findByStatus(otpStatus);
  }

  public void saveAll(List<Otp> sendOtp) {
    otpRepository.saveAll(sendOtp);
  }
}
