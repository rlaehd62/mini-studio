package com.github.rlaehd62.service;

import java.util.List;
import java.util.Optional;

import com.github.rlaehd62.entity.TokenType;
import com.github.rlaehd62.vo.AccountVO;
import com.github.rlaehd62.vo.RequestVO;
import com.github.rlaehd62.vo.TokenVO;

import io.jsonwebtoken.Claims;

public abstract class TokenService
{
	protected final String BLACK_LIST = "TOKEN_BLACK_LIST";
	
	// 한 개 이상의 토큰을 생성
	public abstract Optional<TokenVO> buildTokens(AccountVO vo, RequestVO requestVO, List<TokenType> types);
	
	// 두 종류의 토큰을 모두 반환
	public abstract Optional<TokenVO> packTokens(RequestVO requestVO);
	
	// 두 종류의 토큰을 모두 삭제
	public abstract void unPackTokens(RequestVO requestVO);
	
	// 토큰을 생성
	public abstract Optional<String> createToken(AccountVO vo, TokenType type, RequestVO requestVO);
	
	// 토큰을 저장
	protected abstract void saveToken(String id, TokenType type, String token, RequestVO requestVO);
	
	// 토큰을 삭제
	protected abstract void deleteToken(RequestVO requestVO, List<TokenType> types);
	protected abstract void deleteToken(TokenType type, RequestVO requestVO);
	
	public abstract Optional<String> findToken(TokenType type, RequestVO requestVO);
	public abstract Optional<Claims> verifyToken(String token);
	public abstract boolean isExpired(String token);
}
