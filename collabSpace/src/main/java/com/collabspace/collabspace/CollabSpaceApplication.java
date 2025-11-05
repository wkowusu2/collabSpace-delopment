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
        logger.info("------CollabSpaceApplication started------");
    }


}
