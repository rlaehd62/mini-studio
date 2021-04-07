package com.github.rlaehd62;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BoardServerApplication 
{
	public static void main(String[] args) 
	{
		SpringApplication.run(BoardServerApplication.class, args);
	}
}
