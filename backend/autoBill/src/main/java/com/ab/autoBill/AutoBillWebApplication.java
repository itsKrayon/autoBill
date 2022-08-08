package com.ab.autoBill;

import com.ab.autoBill.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * @author Keyan
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class AutoBillWebApplication {
    private static final Logger logger = LoggerFactory.getLogger(AutoBillWebApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AutoBillWebApplication.class, args);
        logger.info("AutoBillWebApplication running..");
    }

}
