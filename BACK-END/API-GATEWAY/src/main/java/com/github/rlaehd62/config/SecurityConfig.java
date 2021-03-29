package com.github.rlaehd62.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.github.rlaehd62.security.JwtFilter;
import com.github.rlaehd62.security.service.AccountDetailsService;

@EnableWebSecurity 
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired private AccountDetailsService service;
	@Autowired private JwtFilter filter;
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(service).passwordEncoder(passwordEncoder());
	}

	protected void configure(HttpSecurity http) throws Exception
	{   
        http
        	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        	.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
        	.formLogin().disable()
        	.authorizeRequests()	
        		// AUTH-SERVICE
        		.antMatchers(HttpMethod.POST, "/accounts").permitAll()
        		.antMatchers(HttpMethod.POST, "/accounts/login").permitAll()
        		.antMatchers(HttpMethod.GET, "/accounts/logout").authenticated()
        		.antMatchers("/accounts/**").hasRole("ADMIN")
        		.anyRequest().authenticated()
        	.and()
        	.csrf().disable()
        	.headers().frameOptions().disable();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
}
