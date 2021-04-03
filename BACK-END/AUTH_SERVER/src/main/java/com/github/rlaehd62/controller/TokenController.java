package com.github.rlaehd62.controller;

import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.rlaehd62.config.JwtConfig;
import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.entity.TokenType;
import com.github.rlaehd62.service.AccountService;
import com.github.rlaehd62.service.TokenService;
import com.github.rlaehd62.service.implemention.DefaultAccountService;
import com.github.rlaehd62.service.implemention.OptimizedTokenService;
import com.github.rlaehd62.vo.AccountVO;
import com.github.rlaehd62.vo.RequestVO;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/tokens")
public class TokenController
{
	private Logger log;
	private JwtConfig config;
	private TokenService tokenService;
	private AccountService accountService;
	
	@Autowired
	public TokenController(JwtConfig config, DefaultAccountService accountService, OptimizedTokenService optimizedTokenService)
	{
		this.config = config;
		this.log = Logger.getLogger(getClass());
		this.accountService = accountService;
		this.tokenService = optimizedTokenService;
	}
	
	@GetMapping("/verify")
	public ResponseEntity<?> verifyToken(@RequestParam String token)
	{
		Optional<Claims> op = tokenService.verifyToken(token);
		if(!op.isPresent()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ilegal Token Couldn't be Verified!");
		
		AccountVO accountVO = new AccountVO(op.get());
		return ResponseEntity.ok(accountVO);
	}
	
	@PostMapping("/reissue")
	public ResponseEntity<?> reissueAccessToken(@Context HttpServletRequest request, HttpServletResponse response)
	{
		RequestVO requestVO = RequestVO.builder()
				.request(request)
				.response(response)
				.build();
		
		String token = (String) request.getAttribute(TokenType.REFRESH.getName());
		if(Objects.isNull(token)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("REFRESH TOKEN NOT FOUND");
		
		Account account = accountService.getAccount(token);
		AccountVO accountVO = new AccountVO(account);
		
		Optional<String> token_op = tokenService.createToken(accountVO, TokenType.ACCESS, requestVO);
		if(!token_op.isPresent()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰을 생성하는데 실패했습니다.");
		
		return ResponseEntity.ok(token_op.get());
	}
}
