package com.github.rlaehd62.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.github.rlaehd62.service.TokenService;
import com.github.rlaehd62.vo.RequestVO;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class TokenInterceptor implements HandlerInterceptor
{
	
	@Autowired private JwtConfig config;
	@Autowired private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		RequestVO vo = RequestVO.builder()
				.request(request)
				.response(response)
				.build();
		
		String[] headers = { config.getAccess_header(), config.getRefresh_header() };
		for(int i = 0; i < headers.length; i++)
		{
			try
			{
				final String TOKEN = tokenService.findToken(headers[i], vo);
				request.setAttribute(headers[i], TOKEN);
			} catch (Exception e) { return true; }
		}
		
		return true;
	}
}
