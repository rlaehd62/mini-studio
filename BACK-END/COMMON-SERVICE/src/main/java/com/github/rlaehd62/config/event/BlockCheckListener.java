package com.github.rlaehd62.config.event;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.github.rlaehd62.config.event.reques.BlockCheckEvent;
import com.github.rlaehd62.entity.auth.Account;
import com.github.rlaehd62.entity.auth.Block;
import com.github.rlaehd62.vo.account.AccountInfo;
import com.google.common.eventbus.Subscribe;

@Component
public class BlockCheckListener
{
	@Autowired private RestTemplate restTemplate;
	
	@Subscribe
    public void listener(BlockCheckEvent event) 
    {
		Account requester = findAccount(event.getToken());
		String id = requester.getId();
		
		List<Block> blackList = event.getComparsion().getBlocks();
		List<String> list = blackList.stream()
			.map(block -> block.getBlocked())
			.map(blocked -> blocked.getId())
			.collect(Collectors.toList());
		
		event.setSuccessful(list.contains(id));
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
