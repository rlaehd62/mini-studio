package com.github.rlaehd62.service.implemention;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.github.rlaehd62.config.JwtConfig;
import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.repository.AccountRepository;
import com.github.rlaehd62.service.AccountService;
import com.github.rlaehd62.vo.AccountVO;
import com.github.rlaehd62.vo.RequestVO;
import com.github.rlaehd62.vo.TokenType;
import com.github.rlaehd62.vo.TokenVO;

import io.jsonwebtoken.Claims;

@Service("AccountService")
public class DefaultAccountService implements AccountService
{
	@Autowired private AccountRepository accountRepository;
	@Autowired private OptimizedTokenService optimizedTokenService;
	@Autowired private JwtConfig config;
	
	public TokenVO createAccount(AccountVO vo, RequestVO requestVO)
	{
		if(accountRepository.existsById(vo.getId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 계정입니다.");
		Account account = new Account(vo.getId(), vo.getPw(), vo.getUsername());
		accountRepository.save(account);
		
		Optional<TokenVO> token_vo = optimizedTokenService.buildTokens(new AccountVO(account), requestVO, Arrays.asList(TokenType.values()));
		if(!token_vo.isPresent()) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "토큰을 생성하는데 실패했습니다.");
		
		return token_vo.get();
	}
	
	public AccountVO getAccountVO(String id)
	{
		Optional<Account> op = accountRepository.findAccountById(id);
		op.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ACCOUNT NOT FOUND"));
		AccountVO vo = new AccountVO(op.get(), true);
		return vo;
	}
	
	public Account getAccount(String token)
	{
		Optional<Claims> op_claims = optimizedTokenService.verifyToken(token);
		op_claims.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "토큰이 올바르지 않습니다."));
		Claims claims = op_claims.get();
		
		Optional<Account> op_account = accountRepository.findById(claims.getSubject());
		op_account.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 아이디가 존재하지 않습니다."));
		Account account = op_account.get();
		
		return account;
	}

	public TokenVO updateAccount(String token, AccountVO vo, RequestVO requestVO)
	{
		Account account = getAccount(token);
		account.setPw(vo.getPw());
		account.setUsername(vo.getUsername());
		
		accountRepository.save(account);
		optimizedTokenService.unPackTokens(requestVO);
		
		Optional<TokenVO> token_vo = optimizedTokenService.buildTokens(new AccountVO(account), requestVO, Arrays.asList(TokenType.values()));
		if(!token_vo.isPresent()) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "토큰을 생성하는데 실패했습니다.");
		
		return token_vo.get();
	}
	
	public void deleteAccount(String token, RequestVO requestVO)
	{
		Account account = getAccount(token);
		accountRepository.delete(account);
		optimizedTokenService.unPackTokens(requestVO);
	}
	
	public TokenVO verifyAccount(AccountVO vo, RequestVO requestVO)
	{
		boolean isVerified = accountRepository.existsByIdAndPw(vo.getId(), vo.getPw());
		if(!isVerified) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "입력하신 정보가 일치하지 않습니다.");
		
		Optional<Account> op = accountRepository.findAccountById(vo.getId());
		Account account = op.get();
		
		Optional<TokenVO> token_vo = optimizedTokenService.buildTokens(new AccountVO(account), requestVO, Arrays.asList(TokenType.values()));
		if(!token_vo.isPresent()) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "토큰을 생성하는데 실패했습니다.");
		return token_vo.get();
	}
}
