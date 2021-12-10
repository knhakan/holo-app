package com.holoride.demo;

import com.holoride.demo.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DemoApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        LOGGER.info("Welcome to Holoride Application, now you can use the app");
    }

}
