package com.github.rlaehd62.filter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.github.rlaehd62.vo.TokenType;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class TokenInterceptor implements HandlerInterceptor
{
	/**
	 * 쿠키에 존재하는 토큰 값을 Header에 추가해주는 Interceptor
	 */
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		
		for(TokenType type : Arrays.asList(TokenType.values()))
		{
			Optional<Cookie> cookieOptional = findCookie(type.getName(), request);
			if(!cookieOptional.isPresent()) continue;
			else 
			{
				Cookie cookie = cookieOptional.get();
				request.setAttribute(type.getName(), cookie.getValue());
			}
		}
		
		return true;
	}
	
	public Optional<Cookie> findCookie(String header, HttpServletRequest request)
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
}