package com.github.rlaehd62.filter;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.github.rlaehd62.service.RequestUtil;
import com.github.rlaehd62.vo.TokenType;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class TokenInterceptor implements HandlerInterceptor
{
	/**
	 * 쿠키에 존재하는 토큰 값을 Header에 추가해주는 Interceptor
	 */
	
	@Autowired private RequestUtil util;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		for(TokenType type : Arrays.asList(TokenType.values()))
		{
			Optional<String> optional = util.findValue(type, request);
			if(!optional.isPresent()) continue;
			else request.setAttribute(type.getName(), optional.get());
		}
		
		return true;
	}
}