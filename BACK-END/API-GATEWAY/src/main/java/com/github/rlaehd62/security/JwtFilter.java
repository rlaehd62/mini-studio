package com.github.rlaehd62.security;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.rlaehd62.config.JwtConfig;
import com.github.rlaehd62.security.service.AccountDetailsService;
import com.github.rlaehd62.security.service.JwtService;
import com.github.rlaehd62.service.CookieService;
import com.github.rlaehd62.service.RedisService;
import com.github.rlaehd62.vo.AccountVO;

@Component
public class JwtFilter extends OncePerRequestFilter 
{
	private AccountDetailsService detailService;
	private RedisService redisService;
	private CookieService cookieService;
	private JwtConfig config;
	private JwtService jwtService;
	
	@Autowired
	public JwtFilter(AccountDetailsService detailService, RedisService redisService, CookieService cookieService, JwtConfig config, JwtService jwtService)
	{
		this.detailService = detailService;
		this.redisService = redisService;
		this.cookieService = cookieService;
		this.config = config;
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
	{		
		Optional<Cookie> ACCESS_COOKIE = cookieService.getCookie(request, config.getAccess_header());
		Optional<Cookie> REFRESH_COOKIE = cookieService.getCookie(request, config.getRefresh_header());

		try
		{
			ACCESS_COOKIE.ifPresent(cookie -> 
			{
				String token = cookie.getValue();
				if(Objects.nonNull(token))
				{
					String id = jwtService.getID(token);
					
					if(Objects.nonNull(id))
					{
						UserDetails details = detailService.loadUserByUsername(id);
						if(jwtService.validateToken(token, details))
						{
							UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(details, null,details.getAuthorities());
		                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
						}
					}
				}
			});
		} catch (Exception e)
		{
			REFRESH_COOKIE.ifPresent(cookie -> 
			{
				String token = cookie.getValue();
				if(Objects.nonNull(token))
				{
					String id = jwtService.getID(token);
					String savedID = redisService.getData(token);
					
					if(savedID.equals(id))
					{
						UserDetails details = detailService.loadUserByUsername(id);
						if(jwtService.validateToken(token, details))
						{
							UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(details, null,details.getAuthorities());
		                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		                    
		                    AccountVO accountVO = AccountVO.builder()
		                    		.id(details.getUsername())
		                    		.pw(details.getPassword())
		                    		.roles(details.getAuthorities().stream().map(value -> value.getAuthority()).collect(Collectors.toList()))
		                    		.build();
		                    
		                    final String NEW_ACCESS_TOKEN = jwtService.generateToken(accountVO);
		                    Cookie new_cookie = cookieService.createCookie(config.getAccess_header(), NEW_ACCESS_TOKEN, config.getAccess_token_expiration());
		                    response.addCookie(new_cookie);
						}
					}
				}
			});
		}
		
		filterChain.doFilter(request, response);
	}
}
