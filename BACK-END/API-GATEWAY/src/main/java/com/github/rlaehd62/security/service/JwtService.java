package com.github.rlaehd62.security.service;

import java.util.Date;
import java.util.Optional;

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

    public Optional<Claims> extractAllClaims(String token) throws ExpiredJwtException 
    {
    	try
    	{
    		return 
    				Optional.of(Jwts.parser()
    						.setSigningKey(config.getSecret().getBytes())
    						.parseClaimsJws(token)
    						.getBody());
    	} catch (Exception e)
    	{
    		return Optional.empty();
    	}
    }

    public Optional<String> getID(String token) 
    {
    	Optional<Claims> op = extractAllClaims(token);
    	if(!op.isPresent()) return Optional.empty();
        return Optional.of(op.get().getSubject());
    }

    public Boolean isTokenExpired(String token) 
    {
    	Optional<Claims> op = extractAllClaims(token);
    	if(!op.isPresent()) return true;
    	
        final Date expiration = op.get().getExpiration();
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
				.signWith(SignatureAlgorithm.HS256, config.getSecret().getBytes())
				.compact();

        return token;
    }

    public Boolean validateToken(String token, UserDetails userDetails) 
    {
    	Optional<String> op = getID(token);
    	if(!op.isPresent()) return false;
        final String username = op.get();
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

