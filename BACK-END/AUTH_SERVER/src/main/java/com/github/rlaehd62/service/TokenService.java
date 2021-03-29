package com.github.rlaehd62.service;

import java.util.Optional;

import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.vo.RequestVO;
import com.github.rlaehd62.vo.TokenVO;
import com.github.rlaehd62.vo.request.TokenRequestVO;

import io.jsonwebtoken.Claims;

public abstract class TokenService
{
	public abstract TokenVO buildTokens(Account account, RequestVO requestVO);
	public abstract TokenVO packTokens(RequestVO requestVO);
	public abstract void unpackTokens(RequestVO requestVO);
	
	public abstract String createToken(TokenRequestVO vo, RequestVO requestVO, boolean isCached);
	protected abstract void saveToken(TokenRequestVO vo, String token, RequestVO requestVO, boolean isCached);
	
	public abstract void deleteToken(RequestVO requestVO, String... headers);
	public abstract void deleteToken(String header, RequestVO requestVO);
	
	public abstract String findToken(String header, RequestVO requestVO);
	public abstract Optional<Claims> verifyToken(String token);
	public abstract boolean isExpired(String token);
	
}
