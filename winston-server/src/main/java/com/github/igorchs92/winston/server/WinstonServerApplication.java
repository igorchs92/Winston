package com.github.igorchs92.winston.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WinstonServerApplication {
	private static final Logger logger = LoggerFactory.getLogger(WinstonServerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WinstonServerApplication.class, args);
	}
}
