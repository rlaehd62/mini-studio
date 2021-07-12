package com.github.rlaehd62.service.implemention;

import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.github.rlaehd62.config.JwtConfig;
import com.google.common.net.HttpHeaders;

import reactor.core.publisher.Mono;

@Service
public class CookieService
{
	@Autowired private JwtConfig config;
	
	public void insertCookie(HttpServletResponse response, String name, String value, boolean isRemoval)
	{
		ResponseCookie cookie = ResponseCookie
				.from(name, isRemoval ? "" : value)
				.httpOnly(true)
				.path("/")
				.secure(true)
				.sameSite("None")
				.build();

		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString() + (isRemoval ? "; Max-Age=0" : ""));
	}
	
	public Cookie createCookie(String name, String value, int expiration)
	{
		Cookie cookie = new Cookie(name, value);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		return cookie;
	}
	
    public Mono<HttpCookie> getCookie(ServerHttpRequest req, String name)
    {
        final MultiValueMap<String, HttpCookie> cookies = req.getCookies();
        if(cookies.isEmpty() || !cookies.containsKey(name)) return Mono.empty();
        return Mono.just(cookies.getFirst(name));
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
