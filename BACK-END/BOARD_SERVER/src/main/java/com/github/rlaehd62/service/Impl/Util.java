package com.github.rlaehd62.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.rlaehd62.vo.AccountInfo;

@Service
public class Util
{
	private RestTemplate restTemplate;
	
	@Autowired
	public Util(RestTemplate template)
	{
		this.restTemplate = template;
	}
	
	public Optional<AccountInfo> getAccountInfo(String token)
	{
		try
		{
			AccountInfo info = restTemplate.getForObject("http://AUTH-SERVICE/tokens/verify?token="+token, AccountInfo.class);
			return Optional.of(info);
		} 
		
		catch (Exception e)
		{
			return Optional.empty();
		}
	}
	
	public <T> ResponseEntity<?> makeResponseEntity(HttpStatus status, T message)
	{
		return ResponseEntity.status(status).body(message);
	}
}
