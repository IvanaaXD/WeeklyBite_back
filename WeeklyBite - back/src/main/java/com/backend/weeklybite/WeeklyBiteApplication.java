package com.backend.weeklybite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WeeklyBiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeeklyBiteApplication.class, args);
	}

}
