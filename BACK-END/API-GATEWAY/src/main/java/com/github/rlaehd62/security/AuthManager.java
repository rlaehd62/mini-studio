package com.github.rlaehd62.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.github.rlaehd62.security.service.AccountDetailsService;
import com.github.rlaehd62.vo.AccountInfo;

import reactor.core.publisher.Mono;

@Component
public class AuthManager implements ReactiveAuthenticationManager
{
	@Autowired private AccountDetailsService detailService;
	@Autowired private RestTemplate restTemplate;
	
	public Mono<Authentication> authenticate(Authentication authentication)
	{
		try
		{
			String TOKEN = authentication.getCredentials().toString();
			AccountInfo vo = restTemplate.getForObject("http://AUTH-SERVICE/tokens/verify?token="+TOKEN, AccountInfo.class);
			
			AccountDetails details = new AccountDetails(vo.getId(), vo.getPw(), vo.getUsername(), vo.getRoles());
			return Mono.just(new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities()));
		} catch (Exception e)
		{
			return Mono.empty();
		}
	}

}
