package com.github.rlaehd62.config;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.github.rlaehd62.entity.TokenType;
import com.github.rlaehd62.service.implemention.OptimizedTokenService;
import com.github.rlaehd62.vo.RequestVO;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class TokenInterceptor implements HandlerInterceptor
{
	@Autowired private JwtConfig config;
	@Autowired private OptimizedTokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		RequestVO vo = RequestVO.builder()
				.request(request)
				.response(response)
				.build();
		
		for(TokenType type : Arrays.asList(TokenType.values()))
		{
			Optional<String> token_op = tokenService.findToken(type, vo);
			if(!token_op.isPresent()) continue;
			else request.setAttribute(type.getName(), token_op.get());
			System.out.println(type.getName() + ": " + token_op.get());
		}
		
		return true;
	}
}
