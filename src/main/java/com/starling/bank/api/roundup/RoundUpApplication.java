package com.starling.bank.api.roundup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RoundUpApplication {

	static final Logger log = LoggerFactory.getLogger(RoundUpApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RoundUpApplication.class, args);
	}




}
