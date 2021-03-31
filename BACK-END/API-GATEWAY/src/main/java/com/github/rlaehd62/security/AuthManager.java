package com.github.rlaehd62.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.github.rlaehd62.security.service.AccountDetailsService;
import com.github.rlaehd62.security.service.JwtService;

import reactor.core.publisher.Mono;

@Component
public class AuthManager implements ReactiveAuthenticationManager
{
	@Autowired private AccountDetailsService detailService;
	@Autowired private JwtService jwtService;
	
	public Mono<Authentication> authenticate(Authentication authentication)
	{
		try
		{
			String TOKEN = authentication.getCredentials().toString();
			Optional<String> op = jwtService.getID(TOKEN);
			
			if(!op.isPresent()) return Mono.empty();
			String ID = op.get();
			
			Optional<UserDetails> details_op = Optional.ofNullable(detailService.loadUserByUsername(ID));
			if(!details_op.isPresent()) return Mono.empty();
			
			UserDetails details = details_op.get();
			if(jwtService.validateToken(TOKEN, details)) return Mono.just(new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities()));
		} catch (Exception e)
		{
			return Mono.empty();
		}
		
		return Mono.empty();
	}

}
