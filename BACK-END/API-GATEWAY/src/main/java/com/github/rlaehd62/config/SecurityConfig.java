package com.github.rlaehd62.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.github.rlaehd62.security.AuthManager;
import com.github.rlaehd62.security.SecurityContextRepository;

import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig
{
	@Value("${cors.remote_address}") private String REMOTE_ADDRESS;
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
		            	e.printStackTrace();
		                swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
		            });
		        }).and()
				.cors().and()
				.headers().frameOptions().disable().and()
				.csrf().disable()
				.authorizeExchange()
				.pathMatchers(HttpMethod.POST, "/accounts", "/accounts/login", "/accounts/find").permitAll()
				.pathMatchers("/tokens/**").permitAll()
				.anyExchange().authenticated()
				.and()
				.formLogin().disable()
				.authenticationManager(authManager).securityContextRepository(securityRepo)
				.build();
	}
	
    @Bean
    CorsConfigurationSource corsConfiguration() 
    {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.applyPermitDefaultValues();
        corsConfig.setAllowedMethods(Arrays.asList("*"));
        corsConfig.setAllowedHeaders(Arrays.asList("*"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedOrigins(Arrays.asList(REMOTE_ADDRESS));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}
