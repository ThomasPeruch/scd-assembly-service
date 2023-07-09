package com.tperuch.assemblyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AssemblyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssemblyServiceApplication.class, args);
	}

}
