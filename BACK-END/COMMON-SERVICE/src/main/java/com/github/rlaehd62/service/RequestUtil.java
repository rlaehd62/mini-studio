package com.github.rlaehd62.service;

import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.github.rlaehd62.vo.TokenType;

@Service
public class RequestUtil
{
	private Optional<Cookie> findCookie(String header, HttpServletRequest request)
	{
		Cookie[] cookie = request.getCookies();
		if(Objects.isNull(cookie)) return Optional.empty();
		
		for(Cookie ck : cookie)
		{
			String name = ck.getName();
			if(name.equals(header)) return Optional.of(ck);
		}
		
		return Optional.empty();
	}
	
	public Optional<String> findValue(TokenType type, HttpServletRequest request)
	{
		Optional<Cookie> cookie_op = findCookie(type.getName(), request);
		String header = request.getHeader(type.getName());
		
		if(cookie_op.isPresent()) return Optional.of(cookie_op.get().getValue());
		else if(Objects.nonNull(header)) return Optional.of(header);
		return Optional.empty();
	}
}
