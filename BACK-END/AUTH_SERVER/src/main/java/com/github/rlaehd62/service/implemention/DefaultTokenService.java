package com.github.rlaehd62.service.implemention;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.github.rlaehd62.config.JwtConfig;
import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.service.TokenService;
import com.github.rlaehd62.vo.RequestVO;
import com.github.rlaehd62.vo.TokenVO;
import com.github.rlaehd62.vo.request.TokenRequestVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service("TokenService")
public class DefaultTokenService extends TokenService
{
	private JwtConfig config;
	private RedisService redisService;
	private CookieService cookieService;
	
	@Autowired
	public DefaultTokenService(JwtConfig config, RedisService redisService, CookieService cookieService)
	{
		this.config = config;
		this.redisService = redisService;
		this.cookieService = cookieService;
	}
	
	public String createToken(TokenRequestVO vo, RequestVO requestVO, boolean isCached)
	{
		Account account = vo.getAccount();
		Long now = System.currentTimeMillis();
		
		String token = Jwts.builder()
				.setSubject(account.getId())
				.setIssuedAt(new Date(now))
				.setExpiration(new Date(now + (vo.getExpiration() * 1000L)))
				.claim("username", account.getUsername())
				.claim("authorities", account.getRoles().stream().map(value -> value.getRole()).collect(Collectors.toList()))
				.signWith(SignatureAlgorithm.HS256, config.getSecret())
				.compact();
		
		saveToken(vo, token, requestVO, isCached);
		return token;
	}
	
	protected void saveToken(TokenRequestVO vo, String token, RequestVO requestVO, boolean isCached)
	{
		String header = vo.getHeader();
		int expiration = vo.getExpiration();
		
		if(isCached)
		{
			Account account = vo.getAccount();
			redisService.setDataExpire(token, account.getId(), expiration);
		}
		
		Cookie cookie = cookieService.createCookie(header, token, expiration);
		requestVO.getResponse().addCookie(cookie);
	}
	
	public Optional<Claims> verifyToken(String token)
	{
		try
		{
			Claims claims = Jwts.parser()
	                .setSigningKey(config.getSecret())
	                .parseClaimsJws(token)
	                .getBody();
			return Optional.of(claims);
		} 
		
		catch (Exception e)
		{
			e.printStackTrace();
			return Optional.empty();
		}
	}
	
	
	public void deleteToken(RequestVO requestVO, String... headers)
	{
		for(String header : headers) deleteToken(header, requestVO);
	}
	
	public void deleteToken(String header, RequestVO requestVO)
	{
		Optional<Cookie> op = cookieService.findCookie(header, requestVO.getRequest());
		op.ifPresent(ck ->
		{
			redisService.deleteData(ck.getValue());
			Cookie tempCookie = cookieService.createCookie(ck.getName(), "", 0);
			requestVO.getResponse().addCookie(tempCookie);
		});
	}

	public String findToken(String header, RequestVO requestVO)
	{
		try
		{	
			Logger log = Logger.getLogger(this.getClass());
			log.info("findToken 작동");
			
			Optional<Cookie> op = cookieService.findCookie(header, requestVO.getRequest());
			String headerValue = requestVO.getRequest().getHeader(header);
			
			if(op.isPresent()) return op.get().getValue();			
			else if(Objects.nonNull(headerValue)) return headerValue;
			
			throw new Exception("토큰을 찾을 수 없습니다.");
		} catch (Exception e) 
		{
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}

	public TokenVO buildTokens(Account account, RequestVO requestVO)
	{
		TokenRequestVO ACCESS_TOKEN_REQUEST = new TokenRequestVO(config.getAccess_header(), config.getAccess_token_expiration(), account);
		String ACCESS_TOKEN = createToken(ACCESS_TOKEN_REQUEST, requestVO, false);
		
		TokenRequestVO REFRESH_TOKEN_REQUEST = new TokenRequestVO(config.getRefresh_header(), config.getRefresh_token_expiration(), account);
		String REFRESH_TOKEN = createToken(REFRESH_TOKEN_REQUEST, requestVO, true);
		
		return new TokenVO(ACCESS_TOKEN, REFRESH_TOKEN);
	}

	public boolean isExpired(String token)
	{
		try
		{
			Optional<Claims> op = verifyToken(token);
			op.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "토큰이 올바르지 않습니다."));
			
			Claims claims = op.get();
			Date expiration = claims.getExpiration();
			return expiration.before(new Date());
			
		} catch (Exception e)
		{
			return false;
		}
	}

	public TokenVO packTokens(RequestVO requestVO)
	{
		String REFRESH_TOKEN = findToken(config.getRefresh_header(), requestVO);
		String ACCESS_TOKEN = findToken(config.getAccess_header(), requestVO);
		return new TokenVO(ACCESS_TOKEN, REFRESH_TOKEN);
	}
	
	public void unpackTokens(RequestVO requestVO)
	{
		deleteToken(requestVO, config.getRefresh_header(), config.getAccess_header());
	}
}
