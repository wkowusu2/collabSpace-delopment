package com.collabspace.collabspace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
public class CollabSpaceApplication {

    private static final Logger logger = LoggerFactory.getLogger(CollabSpaceApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CollabSpaceApplication.class, args);
        Environment env = context.getEnvironment();

        logger.info("=".repeat(80));
        logger.info("ACTIVE PROFILES: {}", Arrays.toString(env.getActiveProfiles()));
        logger.info("DATABASE URL: {}", maskPassword(env.getProperty("spring.datasource.url")));
        logger.info("DATABASE USERNAME: {}", env.getProperty("spring.datasource.username"));
        logger.info("=".repeat(80));
        logger.info("------CollabSpaceApplication started------");
    }

    private static String maskPassword(String url) {
        if (url == null) return "NOT SET";
        // Mask password in connection string
        return url.replaceAll(":[^:@]+@", ":****@");
    }

}
