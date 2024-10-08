package com.github.rlaehd62.service;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.github.rlaehd62.entity.auth.Account;
import com.github.rlaehd62.vo.account.AccountInfo;

@Service
public class Util
{
	private RestTemplate restTemplate;
	
	@Autowired
	public Util(RestTemplate template)
	{
		this.restTemplate = template;
	}
	
	@Deprecated
	private Optional<AccountInfo> getAccountInfo(String token)
	{
		try
		{
			AccountInfo info = restTemplate.getForObject("http://AUTH-SERVICE/tokens/verify?token="+token, AccountInfo.class);
			return Optional.of(info);
		} 
		
		catch (Exception e)
		{
			e.printStackTrace();
			return Optional.empty();
		}
	}
	
	@Deprecated
	public <T> boolean isMine(Function<Account, Boolean> func, String token)
	{
		Account account = findAccount(token);
		return func.apply(account);
	}
	
	@Deprecated
	public Account findAccount(String token)
	{
		Optional<AccountInfo> accountOptional = getAccountInfo(token);
		accountOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자가 존재하지 않습니다."));
		
		AccountInfo info = accountOptional.get();
		Account account = new Account(info.getId());
		return account;
	}
	
	public <T> ResponseEntity<?> makeResponseEntity(HttpStatus status, T message)
	{
		return ResponseEntity.status(status).body(message);
	}
}
