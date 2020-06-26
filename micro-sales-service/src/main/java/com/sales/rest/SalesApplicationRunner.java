package com.sales.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer
public class SalesApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(SalesApplicationRunner.class, args);
		System.out.println("Sales service started...!!!");
	}

}
