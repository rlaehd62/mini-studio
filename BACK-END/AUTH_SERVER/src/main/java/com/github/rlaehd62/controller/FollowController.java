package com.github.rlaehd62.controller;

import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.rlaehd62.exception.TokenError;
import com.github.rlaehd62.exception.TokenException;
import com.github.rlaehd62.service.FollowService;
import com.github.rlaehd62.service.TokenService;
import com.github.rlaehd62.service.implemention.DefaultFollowService;
import com.github.rlaehd62.service.implemention.OptimizedTokenService;
import com.github.rlaehd62.vo.request.follow.FollowListRequest;
import com.github.rlaehd62.vo.request.follow.FollowToggleEvent;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/follows")
public class FollowController
{
	private FollowService followService;
	private TokenService tokenService;
	
	@Autowired
	public FollowController(DefaultFollowService followService, OptimizedTokenService tokenService)
	{
		this.followService = followService;
		this.tokenService = tokenService;
	}
	
	@GetMapping("")
	public ResponseEntity<?> getList
	(
			@RequestAttribute("ACCESS_TOKEN") String access_token, 
			@RequestParam(required = false) String token
	)
	{
		String key = Objects.nonNull(access_token) ? access_token : token;
		Optional<Claims> op = tokenService.verifyToken(key);
		if(!op.isPresent()) throw new TokenException(TokenError.ILLEGAL_TOKEN);
		
		FollowListRequest request = new FollowListRequest(key);
		return ResponseEntity.ok(followService.getFollowList(request));
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<?> toggle
	(
			@RequestAttribute("ACCESS_TOKEN") String token, 
			@PathVariable String id, 
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response
	)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		FollowToggleEvent event = new FollowToggleEvent(token, id);
		followService.toggle(event);
		return ResponseEntity.ok("팔로우 토글");
	}
}
