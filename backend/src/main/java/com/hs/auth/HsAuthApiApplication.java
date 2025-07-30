package com.hs.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HsAuthApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HsAuthApiApplication.class, args);
	}

}
