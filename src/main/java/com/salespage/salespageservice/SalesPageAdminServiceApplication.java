package com.salespage.salespageservice;

import com.salespage.salespageservice.domains.repositories.base.impl.MongoResourceRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableScheduling
@EnableWebSocket
@EnableMongoRepositories(repositoryBaseClass = MongoResourceRepositoryImpl.class)
public class SalesPageAdminServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesPageAdminServiceApplication.class, args);
    }

}
