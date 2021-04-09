package com.github.rlaehd62.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.github.rlaehd62.security.AuthManager;
import com.github.rlaehd62.security.SecurityContextRepository;

import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig
{
	@Autowired private AuthManager authManager;
	@Autowired private SecurityContextRepository securityRepo;
	
	@Bean
	public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) 
	{
		
		return
				http.exceptionHandling().accessDeniedHandler((swe, e) -> 
		        {
		            return Mono.fromRunnable(() -> 
		            {
		                swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
		            });
		        }).and()
				
				.authorizeExchange()
				.pathMatchers(HttpMethod.POST, "/accounts", "/accounts/login").permitAll()
				.pathMatchers(HttpMethod.GET, "/accounts/logout", "/accounts/myinfo").authenticated()
				.pathMatchers("/accounts/**").hasRole("ADMIN")
				.pathMatchers("/tokens/**").permitAll()
				.anyExchange().authenticated()
				.and().csrf().disable()
				.formLogin().disable()
				.authenticationManager(authManager).securityContextRepository(securityRepo)
				.build();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
}
