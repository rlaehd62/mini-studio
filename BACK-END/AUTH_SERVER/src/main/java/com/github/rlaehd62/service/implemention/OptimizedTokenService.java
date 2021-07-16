package com.github.rlaehd62.service.implemention;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rlaehd62.config.JwtConfig;
import com.github.rlaehd62.service.RequestUtil;
import com.github.rlaehd62.service.TokenService;
import com.github.rlaehd62.vo.AccountVO;
import com.github.rlaehd62.vo.RequestVO;
import com.github.rlaehd62.vo.TokenType;
import com.github.rlaehd62.vo.TokenVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class OptimizedTokenService extends TokenService
{
	private JwtConfig config;
	private RedisService redisService;
	private CookieService cookieService;
	private RequestUtil requestUtil;
	
	@Autowired
	public OptimizedTokenService(RequestUtil requestUtil, JwtConfig config, RedisService redisService, CookieService cookieService)
	{
		this.requestUtil = requestUtil;
		this.config = config;
		this.redisService = redisService;
		this.cookieService = cookieService;
	}

	@Override
	public Optional<TokenVO> buildTokens(AccountVO vo, RequestVO request, List<TokenType> types)
	{
		TokenVO tokenVO = new TokenVO();
		boolean isDone = false;
		
		for(TokenType type : types)
		{
			Optional<String> token_op = createToken(vo, type, request);
			if(!token_op.isPresent()) return Optional.empty();
			if(!isDone) isDone = true;
			
			String token = token_op.get();
			if(type.equals(TokenType.ACCESS)) tokenVO.setACCESS_TOKEN(token);
		}
		
		return (isDone) ? Optional.of(tokenVO) : Optional.empty();
	}

	@Override
	public Optional<TokenVO> packTokens(RequestVO requestVO)
	{
		TokenVO tokenVO = new TokenVO();
		Optional<String> access_op = findToken(TokenType.ACCESS, requestVO);
		access_op.ifPresent(token -> tokenVO.setACCESS_TOKEN(token));
		return (access_op.isPresent()) ? Optional.of(tokenVO) : Optional.empty();
	}

	@Override
	public void unPackTokens(RequestVO requestVO)
	{
		deleteToken(requestVO, Arrays.asList(TokenType.values()));
	}
	
	@Override
	public Optional<String> createToken(AccountVO vo, TokenType type, RequestVO requestVO)
	{
		Long now = System.currentTimeMillis();
		
		try
		{
			String token = Jwts.builder()
					.setSubject(vo.getId())
					.setIssuedAt(new Date(now))
					.claim("username", vo.getUsername())
					.claim("authorities", vo.getRoles())
					.signWith(SignatureAlgorithm.HS256, config.getSecret().getBytes())
					.compact();		
			
			saveToken(vo.getId(), type, token, requestVO);
			return Optional.of(token);
		} catch (Exception e)
		{
			return Optional.empty();
		}
		
	}
	
	@Override
	protected void saveToken(String id, TokenType type, String token, RequestVO requestVO)
	{
		String header = type.getName();
		HttpServletResponse response = requestVO.getResponse();

//		Cookie cookie = cookieService.createCookie(header, token, expiration);
//		response.addCookie(cookie);
		cookieService.insertCookie(response, header, token, false);
		response.addHeader(header, token);
	}

	@Override
	protected void deleteToken(RequestVO requestVO, List<TokenType> types)
	{
		for(TokenType type : types) deleteToken(type, requestVO);
	}

	@Override
	protected void deleteToken(TokenType type, RequestVO requestVO)
	{
		Optional<String> optional = requestUtil.findValue(type, requestVO.getRequest());
		optional.ifPresent(value ->
		{
//			Cookie tempCookie = cookieService.createCookie(type.getName(), "", 0);
//			requestVO.getResponse().addCookie(tempCookie);
			cookieService.insertCookie(requestVO.getResponse(), type.getName(), "", false);
			redisService.addToList(BLACK_LIST, value);
		});
	}

	@Override
	public Optional<String> findToken(TokenType type, RequestVO requestVO)
	{
		String header = type.getName();
		
		Optional<Cookie> op = cookieService.findCookie(header, requestVO.getRequest());
		String headerValue = requestVO.getRequest().getHeader(header);
		
		if(op.isPresent()) return Optional.of(op.get().getValue());		
		else if(Objects.nonNull(headerValue)) return Optional.of(headerValue);
		
		return Optional.empty();
	}

	@Override
	public Optional<Claims> verifyToken(String token)
	{
		try
		{
			List<String> blackList = redisService.getList(BLACK_LIST);
			if(blackList.contains(token)) return Optional.empty();
			
			Claims claims = Jwts.parser()
	                .setSigningKey(config.getSecret().getBytes())
	                .parseClaimsJws(token)
	                .getBody();
			return Optional.of(claims);
		} 
		
		catch (Exception e)
		{
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public boolean isExpired(String token)
	{
		List<String> blackList = redisService.getList(BLACK_LIST);
		Optional<Claims> op = verifyToken(token);
		if(blackList.contains(token) || !op.isPresent()) return false;

		Claims claims = op.get();
		Date expiration = claims.getExpiration();
		return expiration.before(new Date());
	}

}
