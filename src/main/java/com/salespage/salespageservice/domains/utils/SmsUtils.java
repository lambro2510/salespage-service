package com.salespage.salespageservice.domains.utils;

import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.math.BigDecimal;

public class SmsUtils {
  // Find your Account Sid and Token at twilio.com/console
  public static final String ACCOUNT_SID = "AC477c8a8ccc797b5b996db17c24858838";
  public static final String AUTH_TOKEN = "4d2dc761f6e64a0556f133210c81a4f3";

  public static void sendMessage(String otp, String phoneNumber) {

    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    Message message = Message.creator(
            new com.twilio.type.PhoneNumber(Helper.regexPhoneNumber(phoneNumber)),
            new com.twilio.type.PhoneNumber("+18155960805"),
            "Mã otp của bạn là: " + otp)
        .create();

    System.out.println(message.getSid());
  }

  public static void main(String[] args) {
    sendMessage("251002", "+84979163206");
  }
}