package com.github.rlaehd62.controller;

import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.exception.TokenError;
import com.github.rlaehd62.exception.TokenException;
import com.github.rlaehd62.service.AccountService;
import com.github.rlaehd62.service.TokenService;
import com.github.rlaehd62.service.implemention.DefaultAccountService;
import com.github.rlaehd62.service.implemention.OptimizedTokenService;
import com.github.rlaehd62.vo.AccountVO;
import com.github.rlaehd62.vo.RequestVO;
import com.github.rlaehd62.vo.TokenType;

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
