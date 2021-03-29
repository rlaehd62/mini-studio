package com.github.rlaehd62;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AuthServerApplication 
{
	public static void main(String[] args) 
	{
		SpringApplication.run(AuthServerApplication.class, args);
	}
}
