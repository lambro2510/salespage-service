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

  @Bean
  public Drive getService() throws GeneralSecurityException, IOException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    return new Drive.Builder(HTTP_TRANSPORT,
            JacksonFactory.getDefaultInstance(), googleCredential)
            .build();
  }

//  @Bean
//  public GoogleCredential googleCredential() throws GeneralSecurityException, IOException {
//    Collection<String> elenco = new ArrayList<String>();
//    elenco.add("https://www.googleapis.com/auth/gmail.send");
//    elenco.add("https://www.googleapis.com/auth/drive");
//    HttpTransport httpTransport = new NetHttpTransport();
//    JacksonFactory jsonFactory = new JacksonFactory();
//    return GoogleCredential.fromStream(new FileInputStream("oath2.json"))
//            .createScoped(elenco);
//  }


}
