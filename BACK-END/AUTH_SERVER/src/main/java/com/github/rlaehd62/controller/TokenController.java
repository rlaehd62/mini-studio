package com.github.rlaehd62.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.github.rlaehd62.config.JwtConfig;
import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.service.AccountService;
import com.github.rlaehd62.service.TokenService;
import com.github.rlaehd62.service.implemention.CookieService;
import com.github.rlaehd62.service.implemention.DefaultAccountService;
import com.github.rlaehd62.service.implemention.DefaultTokenService;
import com.github.rlaehd62.service.implemention.RedisService;
import com.github.rlaehd62.vo.AccountVO;
import com.github.rlaehd62.vo.RequestVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

@RestController
@RequestMapping("/tokens")
public class TokenController
{
	private Logger log;
	private JwtConfig config;
	private TokenService tokenService;
	private AccountService accountService;
	
	@Autowired
	public TokenController(JwtConfig config, DefaultTokenService tokenService, DefaultAccountService accountService)
	{
		this.config = config;
		this.log = Logger.getLogger(getClass());
		this.tokenService = tokenService;
		this.accountService = accountService;
	}
	
	@GetMapping("/verify")
	public ResponseEntity<?> verityToken(@Context HttpServletRequest request)
	{
		RequestVO requestVO = RequestVO.builder()
				.request(request)
				.build();
		try
		{
			String token = tokenService.findToken(config.getAccess_header(), requestVO);
			Optional<Claims> op = tokenService.verifyToken(token);
			AccountVO accountVO = new AccountVO(op.get());
			
			return ResponseEntity.ok(accountVO);
		} catch (ExpiredJwtException e)
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Expired!");
		} catch (ResponseStatusException e)
		{
			e.printStackTrace();
			return ResponseEntity.status(e.getStatus()).body(e.getMessage());
		}
	}
}
