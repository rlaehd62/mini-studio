package com.github.rlaehd62.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

@Service
public class Util
{
	@Autowired private MonoCookieService cookieService;
	
	/**
	 * 해당 이름의 쿠키 또는 헤더에서 값을 찾습니다.
	 * @return 해당 이름이 갖는 값의 Optional 또는 Optional.Empty
	 */
	
	public <T extends ServerWebExchange> Optional<String> findValue(T t, String name)
	{
		Optional<HttpCookie> cookie_op = cookieService.getCookie(t.getRequest(), name).blockOptional();
		HttpHeaders headers = t.getRequest().getHeaders();
		
		if(cookie_op.isPresent()) return Optional.of(cookie_op.get().getValue());
		else if(headers.containsKey(name)) return Optional.of(headers.get(name).get(0));
		return Optional.empty();
	}
}
