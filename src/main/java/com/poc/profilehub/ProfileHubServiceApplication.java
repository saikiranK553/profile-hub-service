package com.poc.profilehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProfileHubServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfileHubServiceApplication.class, args);
	}

}
