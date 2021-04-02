package com.github.rlaehd62.security;


import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.github.rlaehd62.config.JwtConfig;
import com.github.rlaehd62.security.service.AccountDetailsService;
import com.github.rlaehd62.security.service.JwtService;
import com.github.rlaehd62.service.CookieService;
import com.github.rlaehd62.service.RedisService;
import com.github.rlaehd62.vo.AccountVO;

import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository
{
	@Autowired private AuthManager authManager;
	@Autowired private CookieService cookieService;
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
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();
		HttpHeaders headers = request.getHeaders();
		
		Mono<HttpCookie> ACCESS_COOKIE = cookieService.getCookie(request, config.getAccess_header());
		Mono<HttpCookie> REFRESH_COOKIE = cookieService.getCookie(request, config.getRefresh_header());		
		
		if(headers.containsKey(config.getAccess_header())) return processHeaders(headers);
		return processCookies(response, ACCESS_COOKIE, REFRESH_COOKIE);
	}
	
	private Mono<SecurityContext> processHeaders(HttpHeaders headers)
	{
		String ACCESS_TOKEN = headers.getFirst(config.getAccess_header());
		Authentication auth = new UsernamePasswordAuthenticationToken(ACCESS_TOKEN, ACCESS_TOKEN);
		
		Optional<Authentication> op = authManager.authenticate(auth).blockOptional();
		if(op.isPresent()) return Mono.just(new SecurityContextImpl(op.get()));
		else if(headers.containsKey(config.getRefresh_header()))
		{
			String REFRESH_TOKEN = headers.getFirst(config.getRefresh_header());
			Authentication re_auth = new UsernamePasswordAuthenticationToken(REFRESH_TOKEN, REFRESH_TOKEN);
			
			Optional<Authentication> re_op = authManager.authenticate(re_auth).blockOptional();
			if(re_op.isPresent()) return Mono.just(new SecurityContextImpl(re_op.get()));
			
		}
		
		return Mono.empty();
	}
	
	public Mono<SecurityContext> processCookies(ServerHttpResponse response, Mono<HttpCookie> TOKEN_COOKIE, Mono<HttpCookie> ALTERNATIVE_COOKIE)
	{
		Optional<String> token_op = TOKEN_COOKIE.map(cookie -> cookie.getValue()).blockOptional();
		if(!token_op.isPresent()) return Mono.empty();
		
		String token = token_op.get();
		Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
		
		Optional<Authentication> auth_result = authManager.authenticate(auth).blockOptional();
		if(!auth_result.isPresent()) return processAlternativeCookie(response, ALTERNATIVE_COOKIE);
		return Mono.just(new SecurityContextImpl(auth_result.get()));
	}
	
	private Mono<SecurityContext> processAlternativeCookie(ServerHttpResponse response, Mono<HttpCookie> TOKEN_COOKIE)
	{
		Optional<String> token_op = TOKEN_COOKIE.map(cookie -> cookie.getValue()).blockOptional();
		if(!token_op.isPresent()) return Mono.empty();
		
		String token = token_op.get();
		Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
		
		Optional<Authentication> auth_result = authManager.authenticate(auth).blockOptional();
		if(!auth_result.isPresent()) return Mono.empty();
		
		String savedID = redisService.getData(token);
		Optional<String> op = jwtService.getID(token);
		if(!op.isPresent() || !(op.get()).equals(savedID)) return Mono.empty();
		
		AccountDetails details = (AccountDetails) detailService.loadUserByUsername(op.get());
        AccountVO accountVO = AccountVO.builder()
        		.id(details.getUsername())
        		.pw(details.getPassword())
        		.username(details.getAccount_username())
        		.roles(details.getAuthorities().stream().map(value -> value.getAuthority()).collect(Collectors.toList()))
        		.build();
        
        final String NEW_ACCESS_TOKEN = jwtService.generateToken(accountVO);
        ResponseCookie new_cookie = ResponseCookie.from(config.getAccess_header(), NEW_ACCESS_TOKEN)
        		.maxAge(config.getAccess_token_expiration())
        		.httpOnly(true)
        		.path("/")
        		.build();
    
        response.addCookie(new_cookie);
        response.getHeaders().add(config.getAccess_header(), NEW_ACCESS_TOKEN);
        
        Authentication new_auth = new UsernamePasswordAuthenticationToken(NEW_ACCESS_TOKEN, NEW_ACCESS_TOKEN);
        Authentication verifiedAuth = authManager.authenticate(new_auth).block();
		return Mono.just(new SecurityContextImpl(verifiedAuth));
	}
	

}
