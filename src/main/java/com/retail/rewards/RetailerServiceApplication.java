package com.retail.rewards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This application calculates reward points for customers based on transaction history.
 */
@SpringBootApplication
public class RetailerServiceApplication {

    /**
     * Main method to bootstrap the application.
     *
     * @param args command-line arguments.
     */

    public static void main(String[] args) {
        SpringApplication.run(RetailerServiceApplication.class, args);
    }

}
