package com.salespage.salespageservice.domains.utils;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailRequest {
  public static void sendVerificationCode(String to, String verificationCode) {
    String senderEmail = "lambro25102001@gmail.com";
    String password = "Banhmy09@";

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
              }
            });

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(senderEmail));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
      message.setSubject("Verification Code");
      message.setText("Your verification code is: " + verificationCode);

      Transport.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}
