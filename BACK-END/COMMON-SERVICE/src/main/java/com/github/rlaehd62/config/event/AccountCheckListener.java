package com.github.rlaehd62.config.event;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.github.rlaehd62.config.event.reques.AccountCheckEvent;
import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.vo.account.AccountInfo;
import com.google.common.eventbus.Subscribe;

@Component
public class AccountCheckListener
{
	@Autowired private RestTemplate restTemplate;
	
	@Subscribe
    public void listener(AccountCheckEvent event) 
    {
		Account user = findAccount(event.getToken());
		if(event.isPrivileged())
		{
			event.setPrivilegedResult(user);
			return;
		}
		
		String comparableID = event.getComparsion().getId();
		event.setSuccessful(comparableID.equals(user.getId()));
    }
	
	private Optional<Account> getAccountInfo(String token)
	{
		try
		{
			AccountInfo info = restTemplate.getForObject("http://AUTH-SERVICE/tokens/verify?token="+token, AccountInfo.class);
			return Optional.of(new Account(info.getId()));
		} 
		
		catch (Exception e)
		{
			e.printStackTrace();
			return Optional.empty();
		}
	}
	
	private Account findAccount(String token)
	{
		Optional<Account> accountOptional = getAccountInfo(token);
		accountOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자가 존재하지 않습니다."));
		Account account = accountOptional.get();
		return account;
	}
}
