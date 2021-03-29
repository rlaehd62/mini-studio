package com.github.rlaehd62;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ApiGatewayApplicationTests 
{
	@Autowired com.github.rlaehd62.security.service.AccountDetailsService service;
	
	@Test
	void detailsTest() 
	{
		UserDetails details = service.loadUserByUsername("root");
		assertNotNull(details);
	}

}
