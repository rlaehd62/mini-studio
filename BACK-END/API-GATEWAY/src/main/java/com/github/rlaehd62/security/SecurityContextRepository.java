package com.github.rlaehd62.security;


import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.github.rlaehd62.config.JwtConfig;
import com.github.rlaehd62.security.service.AccountDetailsService;
import com.github.rlaehd62.security.service.JwtService;
import com.github.rlaehd62.service.MultiFunction;
import com.github.rlaehd62.service.RedisService;
import com.github.rlaehd62.service.Util;

import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository
{
	@Autowired private AuthManager authManager;
	@Autowired private Util utilService;
	@Autowired private JwtConfig config;

	public Mono<Void> save(ServerWebExchange exchange, SecurityContext context)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public Mono<SecurityContext> load(ServerWebExchange exchange)
	{
		MultiFunction<Authentication, String, Mono<SecurityContext>> func = (auth, token) -> Mono.just(new SecurityContextImpl(auth));
		String ACCESS = config.getAccess_header();
		return process(ACCESS, exchange, func);
	}
	
	private Mono<SecurityContext> process
	(
			String name, ServerWebExchange exchange, 
			MultiFunction<Authentication, String, Mono<SecurityContext>> function
	)
	{
		String HEADER = name;
		Optional<String> optional = utilService.findValue(exchange, HEADER);
		
		if(Objects.isNull(function)) return Mono.empty();
		else if(optional.isPresent())
		{
			String TOKEN = optional.get();
			Authentication auth = new UsernamePasswordAuthenticationToken(TOKEN, TOKEN);
			Optional<Authentication> authOptional = authManager.authenticate(auth).blockOptional();
			if(authOptional.isPresent()) return function.apply(authOptional.get(), TOKEN);
		}
		
		return Mono.empty();
	}
}
