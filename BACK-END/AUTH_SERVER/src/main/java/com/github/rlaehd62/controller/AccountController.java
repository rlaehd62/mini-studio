package com.github.rlaehd62.controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.github.rlaehd62.config.JwtConfig;
import com.github.rlaehd62.entity.TokenType;
import com.github.rlaehd62.service.AccountService;
import com.github.rlaehd62.service.TokenService;
import com.github.rlaehd62.service.implemention.DefaultAccountService;
import com.github.rlaehd62.service.implemention.OptimizedTokenService;
import com.github.rlaehd62.vo.AccountVO;
import com.github.rlaehd62.vo.RequestVO;
import com.github.rlaehd62.vo.TokenVO;

import io.jsonwebtoken.ExpiredJwtException;

@RestController
@RequestMapping("/accounts")
public class AccountController
{
	private AccountService accountService;
	private TokenService tokenService;
	private JwtConfig config;

	@Autowired
	public AccountController(JwtConfig config, DefaultAccountService accountService, OptimizedTokenService tokenService)
	{
		this.config = config;
		this.accountService = accountService;
		this.tokenService = tokenService;
	}
	
	@PostMapping("")
	public ResponseEntity<?> createAccount(@RequestBody AccountVO vo, @Context HttpServletResponse response)
	{
		RequestVO requestVO = RequestVO.builder()
				.response(response)
				.build();
		try
		{
			TokenVO tokenVO = accountService.createAccount(vo, requestVO);
			return ResponseEntity.ok(tokenVO);
		} catch(ResponseStatusException e) 
		{
			e.printStackTrace();
			return ResponseEntity.status(e.getStatus()).body(e.getMessage());
		}
	}
	
	
	@PatchMapping("")
	public ResponseEntity<?> updateAccount
	(
			@RequestParam (required = false) String pw, 
			@RequestParam (required = false) String username, 
			@Context HttpServletRequest request, @Context HttpServletResponse response
	)
	{
		RequestVO requestVO = RequestVO.builder()
				.request(request)
				.response(response)
				.build();
		
		try
		{
			String token = (String) request.getAttribute(TokenType.ACCESS.getName());
			if(Objects.isNull(token)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ACCESS TOKEN NOT FOUND");
			
			AccountVO vo = new AccountVO(accountService.getAccount(token), true);
			if(Objects.nonNull(pw)) vo.setPw(pw);
			if(Objects.nonNull(username)) vo.setUsername(username);
			
			TokenVO tokenVO = accountService.updateAccount(token, vo, requestVO);
			return ResponseEntity.ok(tokenVO);
		} catch(ResponseStatusException e) 
		{
			e.printStackTrace();
			return ResponseEntity.status(e.getStatus()).body(e.getMessage());
		}
	}
	
	@DeleteMapping("")
	public ResponseEntity<?> deleteAccount(@Context HttpServletRequest request, @Context HttpServletResponse response)
	{
		RequestVO requestVO = RequestVO.builder()
				.request(request)
				.response(response)
				.build();
		try
		{
			String token = (String) request.getAttribute(TokenType.ACCESS.getName());
			if(Objects.isNull(token)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ACCESS TOKEN NOT FOUND");
			
			accountService.deleteAccount(token, requestVO);
			return ResponseEntity.ok("Account Deleted!");
		} catch (ExpiredJwtException e)
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Expired!");
		} catch (ResponseStatusException e)
		{
			e.printStackTrace();
			return ResponseEntity.status(e.getStatus()).body(e.getMessage());
		}
	}
	
	@GetMapping("")
	public ResponseEntity<?> getAccount(@RequestParam String id)
	{
		AccountVO vo = accountService.getAccountVO(id);
		return ResponseEntity.ok(vo);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> loginAccount(@RequestBody AccountVO vo, @Context HttpServletResponse response)
	{
		RequestVO requestVO = RequestVO.builder()
				.response(response)
				.build();
		try
		{
			TokenVO tokenVO = accountService.verifyAccount(vo, requestVO);
			return ResponseEntity.ok(tokenVO);
		} catch(ResponseStatusException e) 
		{
			e.printStackTrace();
			return ResponseEntity.status(e.getStatus()).body(e.getMessage());
		}
	}
	
	@GetMapping("/logout")
	public ResponseEntity<?> logoutAccount(@Context HttpServletRequest request, @Context HttpServletResponse response)
	{
		RequestVO requestVO = RequestVO.builder()
				.request(request)
				.response(response)
				.build();
		try
		{
			tokenService.unPackTokens(requestVO);
			return ResponseEntity.ok("You just logged out.");
		} catch (ResponseStatusException e)
		{
			return ResponseEntity.status(e.getStatus()).body(e.getMessage());
		}
	}
}
