package org.crazymages.bankingspringproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class BankingSpringProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingSpringProjectApplication.class, args);
    }

}
