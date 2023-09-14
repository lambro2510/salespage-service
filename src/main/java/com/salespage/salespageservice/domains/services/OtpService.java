package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.domains.entities.Otp;
import com.salespage.salespageservice.domains.entities.types.OtpStatus;
import com.salespage.salespageservice.domains.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OtpService extends BaseService{
  public final Long expireOtpTime = 600000L;
  public List<Otp> getWaitingOtp() {
    List<Otp> waitingOtps = otpStorage.findByStatus(OtpStatus.WAITING);
    List<Otp> sendOtp = new ArrayList<>();
    for(Otp otp : waitingOtps){
      if(otp.getCreatedAt() + expireOtpTime > DateUtils.nowInMillis()){
        otp.setStatus(OtpStatus.SEND);
        sendOtp.add(otp);
      }
    }
    otpStorage.saveAll(sendOtp);
    return sendOtp;
  }
}
