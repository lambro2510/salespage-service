package com.salespage.salespageservice.domains.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;

@Configuration
public class GoogleApiConfig {

  @Autowired
  @Lazy
  private GoogleCredential googleCredential;

  @Value("${google.api.url}")
  private String url;

  @Bean
  public Drive getService() throws GeneralSecurityException, IOException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    return new Drive.Builder(HTTP_TRANSPORT,
            JacksonFactory.getDefaultInstance(), googleCredential)
            .build();
  }

//  @Bean
//  public Gmail getGmailService() throws GeneralSecurityException, IOException {
//    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//    Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JacksonFactory.getDefaultInstance(), googleCredential)
//            .setApplicationName("salespage")
//            .build();
//    return service;
//  }

  @Bean
  public GoogleCredential googleCredential() throws GeneralSecurityException, IOException {
    Collection<String> elenco = new ArrayList<String>();
    elenco.add("https://www.googleapis.com/auth/drive");
    HttpTransport httpTransport = new NetHttpTransport();
    JacksonFactory jsonFactory = new JacksonFactory();
    return GoogleCredential.fromStream(new FileInputStream("salepage-374708-1ef203c3e998.json"))
            .createScoped(elenco);
  }


}
