package com.github.rlaehd62.service.implemention;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.rlaehd62.config.JwtConfig;
import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.exception.AccountError;
import com.github.rlaehd62.exception.AccountException;
import com.github.rlaehd62.exception.TokenError;
import com.github.rlaehd62.exception.TokenException;
import com.github.rlaehd62.repository.AccountRepository;
import com.github.rlaehd62.service.AccountService;
import com.github.rlaehd62.service.MailService;
import com.github.rlaehd62.service.Util;
import com.github.rlaehd62.vo.AccountCreateRequest;
import com.github.rlaehd62.vo.AccountVO;
import com.github.rlaehd62.vo.MailVO;
import com.github.rlaehd62.vo.RequestVO;
import com.github.rlaehd62.vo.TokenType;
import com.github.rlaehd62.vo.TokenVO;
import com.github.rlaehd62.vo.request.AccountDeleteRequest;
import com.github.rlaehd62.vo.request.AccountFindRequest;
import com.github.rlaehd62.vo.request.AccountListRequest;
import com.github.rlaehd62.vo.request.AccountRequest;
import com.github.rlaehd62.vo.request.AccountUpdateRequest;
import com.github.rlaehd62.vo.response.Info;

import io.jsonwebtoken.Claims;

@Service("AccountService")
public class DefaultAccountService implements AccountService
{
	@Value("${spring.mail.username}") private String from;
	@Autowired private AccountRepository accountRepository;
	@Autowired private OptimizedTokenService optimizedTokenService;
	@Autowired private JwtConfig config;
	@Autowired private Util util;
	@Autowired private MailService mailService;
	
	public TokenVO createAccount(AccountCreateRequest request, RequestVO requestVO)
	{
		String ID = request.getId();
		
		if(accountRepository.existsById(ID)) throw new AccountException(AccountError.ACCOUNT_EXIST);
		Account account = new Account(request);
		accountRepository.save(account);
		
		Optional<TokenVO> token_vo = optimizedTokenService.buildTokens(new AccountVO(account), requestVO, Arrays.asList(TokenType.values()));
		if(!token_vo.isPresent()) throw new TokenException(TokenError.TOKEN_CREATION_FAILED);
		
		return token_vo.get();
	}
	
	@Deprecated
	public AccountVO getAccountVO(AccountRequest request)
	{
		Account account = getAccount(request.getToken());
		return new AccountVO(account);
	}
	
	public Account getAccount(String token)
	{
		Optional<Claims> op_claims = optimizedTokenService.verifyToken(token);
		op_claims.orElseThrow(() -> new TokenException(TokenError.ILLEGAL_TOKEN));
		Claims claims = op_claims.get();
		
		Optional<Account> op_account = accountRepository.findById(claims.getSubject());
		op_account.orElseThrow(() -> new AccountException(AccountError.ACCOUNT_NOT_FOUND));
		Account account = op_account.get();
		
		return account;
	}

	public TokenVO updateAccount(AccountUpdateRequest request, RequestVO requestVO)
	{
		String token = request.getToken();
		Account account = getAccount(token);
		
		String pw = account.getPw();
		String mail = account.getEmail();
		String username = account.getUsername();
		
		if(Objects.nonNull(request.getPw())) account.setPw(request.getPw());
		if(Objects.nonNull(request.getEmail()) && !request.getEmail().equals(mail)) account.setEmail(request.getEmail());
		if(Objects.nonNull(request.getUsername()) && !request.getUsername().equals(username)) account.setUsername(request.getUsername());
		
		Function<Account, Boolean> function = (input) ->
		{
			String tempID = input.getId();
			String accountID = account.getId();	
			return tempID.equals(accountID);
		};
		
		if(!util.isMine(function, token)) throw new AccountException(AccountError.ACCOUNT_NO_PERMISSION);
		
		accountRepository.save(account);
		optimizedTokenService.unPackTokens(requestVO);
		
		Optional<TokenVO> token_vo = optimizedTokenService.buildTokens(new AccountVO(account), requestVO, Arrays.asList(TokenType.values()));
		if(!token_vo.isPresent()) throw new TokenException(TokenError.TOKEN_CREATION_FAILED);
		
		return token_vo.get();
	}
	
	public void deleteAccount(AccountDeleteRequest request, RequestVO requestVO)
	{
		String token = request.getToken();
		Account account = getAccount(token);
		accountRepository.delete(account);
		optimizedTokenService.unPackTokens(requestVO);
	}
	
	public TokenVO verifyAccount(AccountVO vo, RequestVO requestVO)
	{
		boolean isVerified = accountRepository.existsByIdAndPw(vo.getId(), vo.getPw());
		if(!isVerified) throw new AccountException(AccountError.ACCOUNT_DENY);
		
		Optional<Account> op = accountRepository.findAccountById(vo.getId());
		Account account = op.get();
		
		Optional<TokenVO> token_vo = optimizedTokenService.buildTokens(new AccountVO(account), requestVO, Arrays.asList(TokenType.values()));
		if(!token_vo.isPresent()) throw new TokenException(TokenError.TOKEN_CREATION_FAILED);
		return token_vo.get();
	}

	@Override
	public List<Info> getAccountList(AccountListRequest request)
	{
		return accountRepository.findAllByIdContaining(request.getId(), request.getPageable())
				.stream().map(value -> new Info(value.getId(), value.getUsername()))
				.collect(Collectors.toList());
	}

	@Override
	public void findAccount(AccountFindRequest request)
	{
		Optional<Account> op = accountRepository.findAccountById(request.getId());
		op.orElseThrow(() -> new AccountException(AccountError.ACCOUNT_NOT_FOUND));
		
		Account account = op.get();
		String ID = account.getEmail();
		String email = account.getEmail();
		
		if(!request.getEmail().equals(email)) throw new AccountException(AccountError.ACCOUNT_DENY);
		account.setPw(UUID.randomUUID().toString().replaceAll("-", ""));
		accountRepository.save(account);
		
		MailVO mail = new MailVO(from, email, ID + "님의 정보", "임시로 비밀번호를 발급했습니다 (반드시 변경해주세요)\n비밀번호: " + account.getPw());
		mailService.mailSend(mail);
	}
}
