package com.github.rlaehd62.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.rlaehd62.exception.TokenError;
import com.github.rlaehd62.exception.TokenException;
import com.github.rlaehd62.service.TokenService;
import com.github.rlaehd62.service.implemention.OptimizedTokenService;
import com.github.rlaehd62.vo.AccountVO;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/tokens")
public class TokenController
{
	private TokenService tokenService;

	@Autowired
	public TokenController(OptimizedTokenService optimizedTokenService)
	{
		this.tokenService = optimizedTokenService;
	}
	
	@GetMapping("/verify")
	public ResponseEntity<?> verifyToken(@RequestParam String token)
	{
		Optional<Claims> op = tokenService.verifyToken(token);
		if(!op.isPresent()) throw new TokenException(TokenError.ILLEGAL_TOKEN);
		
		AccountVO accountVO = new AccountVO(op.get());
		return ResponseEntity.ok(accountVO);
	}
}
