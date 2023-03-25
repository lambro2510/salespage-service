package com.salespage.salespageservice;

import com.salespage.salespageservice.domains.repositories.base.impl.MongoResourceRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(repositoryBaseClass = MongoResourceRepositoryImpl.class)
public class SalesPageAdminServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SalesPageAdminServiceApplication.class, args);
  }

}
