package com.github.rlaehd62.service.implemention;

import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rlaehd62.config.JwtConfig;

@Service
public class CookieService
{
	@Autowired private JwtConfig config;
	
	public Cookie createCookie(String name, String value, int expiration)
	{
		Cookie cookie = new Cookie(name, value);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(expiration);
		return cookie;
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
