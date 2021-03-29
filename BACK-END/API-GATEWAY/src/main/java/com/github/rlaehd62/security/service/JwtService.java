package com.github.rlaehd62.security.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.github.rlaehd62.config.JwtConfig;
import com.github.rlaehd62.vo.AccountVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService
{
	@Autowired private JwtConfig config;

    public Claims extractAllClaims(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(config.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    public String getID(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public String generateToken(AccountVO vo) {
        return doGenerateToken(vo, config.getAccess_token_expiration());
    }

    public String generateRefreshToken(AccountVO vo) {
        return doGenerateToken(vo, config.getRefresh_token_expiration());
    }

    public String doGenerateToken(AccountVO vo, long expireTime) 
    {    
		Long now = System.currentTimeMillis();
		String token = Jwts.builder()
				.setSubject(vo.getId())
				.setIssuedAt(new Date(now))
				.setExpiration(new Date(now + (expireTime * 1000L)))
				.claim("username", vo.getUsername())
				.claim("authorities", vo.getRoles())
				.signWith(SignatureAlgorithm.HS256, config.getSecret())
				.compact();

        return token;
    }

    public Boolean validateToken(String token, UserDetails userDetails) 
    {
        final String username = getID(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

