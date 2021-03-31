package com.github.rlaehd62.service;

import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import reactor.core.publisher.Mono;

@Service
public class CookieService
{
    public Cookie createCookie(String cookieName, String value, int expiration)
    {
        Cookie token = new Cookie(cookieName,value);
        token.setHttpOnly(true);
        token.setMaxAge(expiration);
        token.setPath("/");
        return token;
    }
    
    public Mono<HttpCookie> getCookie(ServerHttpRequest req, String name)
    {
        final MultiValueMap<String, HttpCookie> cookies = req.getCookies();
        if(cookies.isEmpty() || !cookies.containsKey(name)) return Mono.empty();
        return Mono.just(cookies.getFirst(name));
    }
    
	public Optional<Cookie> getCookie(HttpServletRequest req, String name)
	{
        final Cookie[] cookies = req.getCookies();
        if(Objects.isNull(cookies)) return Optional.empty();
        for(Cookie cookie : cookies)
        {
        	String cookieName= cookie.getName();
            if(cookieName.equals(name)) return Optional.of(cookie);
        }
        
        return Optional.empty();
	}
}
