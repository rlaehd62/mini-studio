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
import org.springframework.security.core.userdetails.UserDetails;
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
	
	public Mono<SecurityContext> processCookies(ServerHttpResponse response, Mono<HttpCookie> ACCESS_COOKIE, Mono<HttpCookie> REFRESH_COOKIE)
	{
		Optional<String> ACCESS_TOKEN_OP = ACCESS_COOKIE.map(cookie -> cookie.getValue()).blockOptional();
		Optional<String> REFRESH_TOKEN_OP = REFRESH_COOKIE.map(cookie -> cookie.getValue()).blockOptional();
		
		if(ACCESS_TOKEN_OP.isPresent())
		{
			String ACCESS_TOKEN = ACCESS_TOKEN_OP.get();
			Authentication access_auth = new UsernamePasswordAuthenticationToken(ACCESS_TOKEN, ACCESS_TOKEN);
			Optional<Authentication> ACCESS_OP = authManager.authenticate(access_auth).blockOptional();
			if(ACCESS_OP.isPresent()) return Mono.just(new SecurityContextImpl(ACCESS_OP.get()));
			
			else if(REFRESH_TOKEN_OP.isPresent())
			{
				String REFRESH_TOKEN = REFRESH_TOKEN_OP.get();
				
				Optional<String> op = jwtService.getID(REFRESH_TOKEN);
				if(!op.isPresent()) return Mono.empty();
				
				String ID = op.get();
				String savedID = redisService.getData(REFRESH_TOKEN);
				
				if(ID.equals(savedID))
				{
					UserDetails details = detailService.loadUserByUsername(ID);
					
                    AccountVO accountVO = AccountVO.builder()
                    		.id(details.getUsername())
                    		.pw(details.getPassword())
                    		.roles(details.getAuthorities().stream().map(value -> value.getAuthority()).collect(Collectors.toList()))
                    		.build();
                    
                    final String NEW_ACCESS_TOKEN = jwtService.generateToken(accountVO);
                    ResponseCookie new_cookie = ResponseCookie.from(config.getAccess_header(), NEW_ACCESS_TOKEN)
                    		.maxAge(config.getAccess_token_expiration())
                    		.httpOnly(true)
                    		.path("/")
                    		.build();
                
                    response.addCookie(new_cookie);
                    
                    Authentication new_access_auth = new UsernamePasswordAuthenticationToken(NEW_ACCESS_TOKEN, NEW_ACCESS_TOKEN);
                    return Mono.just(new SecurityContextImpl(new_access_auth));
				}
			}
		}
		
		return Mono.empty();
	}

}
