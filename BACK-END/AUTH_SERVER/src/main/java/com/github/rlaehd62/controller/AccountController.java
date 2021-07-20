package com.github.rlaehd62.controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.github.rlaehd62.config.JwtConfig;
import com.github.rlaehd62.entity.auth.Account;
import com.github.rlaehd62.exception.AccountError;
import com.github.rlaehd62.exception.AccountException;
import com.github.rlaehd62.exception.TokenError;
import com.github.rlaehd62.exception.TokenException;
import com.github.rlaehd62.service.AccountService;
import com.github.rlaehd62.service.BlockService;
import com.github.rlaehd62.service.TokenService;
import com.github.rlaehd62.service.implemention.DefaultAccountService;
import com.github.rlaehd62.service.implemention.DefaultBlockService;
import com.github.rlaehd62.service.implemention.OptimizedTokenService;
import com.github.rlaehd62.vo.AccountVO;
import com.github.rlaehd62.vo.RequestVO;
import com.github.rlaehd62.vo.TokenType;
import com.github.rlaehd62.vo.TokenVO;
import com.github.rlaehd62.vo.account.AccountCreateRequest;
import com.github.rlaehd62.vo.request.account.AccountDeleteRequest;
import com.github.rlaehd62.vo.request.account.AccountFindRequest;
import com.github.rlaehd62.vo.request.account.AccountListRequest;
import com.github.rlaehd62.vo.request.account.AccountUpdateRequest;
import com.github.rlaehd62.vo.response.MyInfo;

import io.jsonwebtoken.ExpiredJwtException;

@RestController
@RequestMapping("/accounts")
public class AccountController
{
	private AccountService accountService;
	private TokenService tokenService;
	private BlockService blockService;
	private JwtConfig config;

	@Autowired
	public AccountController(JwtConfig config, DefaultAccountService accountService, OptimizedTokenService tokenService, DefaultBlockService blockService)
	{
		this.config = config;
		this.accountService = accountService;
		this.tokenService = tokenService;
		this.blockService = blockService;
	}
	
	@PostMapping("")
	public ResponseEntity<?> createAccount(AccountCreateRequest request, @Context HttpServletRequest sRequest, @Context HttpServletResponse response)
	{
		RequestVO requestVO = RequestVO.builder()
				.request(sRequest)
				.response(response)
				.build();
		
		TokenVO tokenVO = accountService.createAccount(request, requestVO);
		return ResponseEntity.ok(tokenVO);
	}
	
	
	@PatchMapping("")
	public ResponseEntity<?> updateAccount
	(
			AccountUpdateRequest updateRequest,
			@RequestAttribute("ACCESS_TOKEN") String token,
			@Context HttpServletRequest request, @Context HttpServletResponse response
	)
	{
		RequestVO requestVO = RequestVO.builder()
				.request(request)
				.response(response)
				.build();
		
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		updateRequest.setToken(token);
		TokenVO tokenVO = accountService.updateAccount(updateRequest, requestVO);
		return ResponseEntity.ok(tokenVO);
	}
	
	@DeleteMapping("")
	public ResponseEntity<?> deleteAccount
	(
			@RequestAttribute("ACCESS_TOKEN") String token, 
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response
	)
	{
		RequestVO requestVO = RequestVO.builder()
				.request(request)
				.response(response)
				.build();
		try
		{
			if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
			AccountDeleteRequest delRequest = new AccountDeleteRequest(token);
			
			accountService.deleteAccount(delRequest, requestVO);
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
	public ResponseEntity<?> getAccounts
	(
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
			@RequestParam String username
	)
	{
		AccountListRequest request = new AccountListRequest(username, pageable);
		return ResponseEntity.ok(accountService.getAccountList(request));
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> loginAccount(AccountVO vo, @Context HttpServletRequest request, @Context HttpServletResponse response)
	{
		RequestVO requestVO = RequestVO.builder()
				.request(request)
				.response(response)
				.build();

		TokenVO tokenVO = accountService.verifyAccount(vo, requestVO);
		return ResponseEntity.ok(tokenVO);
	}
	
	@GetMapping("/logout")
	public ResponseEntity<?> logoutAccount(@Context HttpServletRequest request, @Context HttpServletResponse response)
	{
		RequestVO requestVO = RequestVO.builder()
				.request(request)
				.response(response)
				.build();
		
		tokenService.unPackTokens(requestVO);
		return ResponseEntity.ok("You just logged out.");
	}
	
	@GetMapping("/myinfo")
	public ResponseEntity<?> getMyInfo(@RequestAttribute("ACCESS_TOKEN") String token, @Context HttpServletRequest request, @Context HttpServletResponse response)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		Account account = accountService.getAccount(token);
		MyInfo info = 
				MyInfo.builder()
				.id(account.getId())
				.username(account.getUsername())
				.email(account.getEmail())
				.build();
		return ResponseEntity.ok(info);
	}
	
	@PostMapping("/find")
	public ResponseEntity<?> findMyInfo
	(
			@RequestParam String id,
			@RequestParam String email,
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response
	)
	{
		if(Objects.nonNull(request.getAttribute(TokenType.ACCESS.getName()))) throw new AccountException(AccountError.ACCOUNT_DENY);
		AccountFindRequest findRequest = new AccountFindRequest(id, email);
		accountService.findAccount(findRequest);
		return ResponseEntity.ok("정보 발송 완료");
	}
}
