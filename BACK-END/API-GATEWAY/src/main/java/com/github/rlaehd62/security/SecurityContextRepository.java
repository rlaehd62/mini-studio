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
	@Autowired private AccountDetailsService detailService;
	@Autowired private JwtService jwtService;
	@Autowired private JwtConfig config;
	@Autowired private RedisService redisService;
	
	public Mono<Void> save(ServerWebExchange exchange, SecurityContext context)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public Mono<SecurityContext> load(ServerWebExchange exchange)
	{
		MultiFunction<Authentication, String, Mono<SecurityContext>> func = (auth, token) -> { return Mono.just(new SecurityContextImpl(auth)); };
		MultiFunction<Authentication, String, Mono<SecurityContext>> altFunc = (auth, token) ->
		{
			String ID = auth.getName();
			String savedID = redisService.getData(token);
			if(!savedID.equals(ID)) return Mono.empty();
	        ResponseStatusException exception = new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "액세스 토큰의 재발급이 필요합니다.");
			return Mono.error(exception);
		};
		
		String ACCESS = config.getAccess_header();
		return process(ACCESS, exchange, func, altFunc, true);
	}
	
	private Mono<SecurityContext> process
	(
			String name, ServerWebExchange exchange, 
			MultiFunction<Authentication, String, Mono<SecurityContext>> function,
			MultiFunction<Authentication, String, Mono<SecurityContext>> altFunc,
			boolean hasAlternativity
	)
	{
		String HEADER = name;
		Optional<String> optional = utilService.findValue(exchange, HEADER);
		
		if(Objects.isNull(function) || (hasAlternativity && Objects.isNull(altFunc))) return Mono.empty();
		else if(optional.isPresent())
		{
			String TOKEN = optional.get();
			Authentication auth = new UsernamePasswordAuthenticationToken(TOKEN, TOKEN);
			Optional<Authentication> authOptional = authManager.authenticate(auth).blockOptional();
			
			if(authOptional.isPresent())
			{
				return function.apply(authOptional.get(), TOKEN);
			} else if(hasAlternativity) return process(config.getRefresh_header(), exchange, altFunc, null, false);
			
		} else if(hasAlternativity) return process(config.getRefresh_header(), exchange, altFunc, null, false);
		return Mono.empty();
	}
}
