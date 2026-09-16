package com.h4h.employeeportal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.h4h.employeeportal"})
public class EmployeePortalApplication {

    private static final Logger log = LoggerFactory.getLogger(EmployeePortalApplication.class);

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        SpringApplication.run(EmployeePortalApplication.class, args);

        long duration = System.currentTimeMillis() - start;
        log.info("Application started in {} ms", duration);
    }
}
