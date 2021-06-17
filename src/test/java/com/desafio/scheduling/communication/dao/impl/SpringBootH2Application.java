package com.desafio.scheduling.communication.dao.impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EntityScan("com.desafio.scheduling.communication.dao.*")   
public class SpringBootH2Application extends SpringBootServletInitializer{

    public static void main(String... args) {
        SpringApplication.run(SpringBootH2Application.class, args);
    }
}
