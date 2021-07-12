package com.github.rlaehd62.controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.rlaehd62.exception.TokenError;
import com.github.rlaehd62.exception.TokenException;
import com.github.rlaehd62.request.MusicDeleteRequest;
import com.github.rlaehd62.request.MusicListRequest;
import com.github.rlaehd62.request.MusicReadRequest;
import com.github.rlaehd62.request.MusicUpdateRequest;
import com.github.rlaehd62.request.MusicUploadRequest;
import com.github.rlaehd62.service.MusicService;

@RestController
@RequestMapping("/music")
public class MusicController
{
	private MusicService musicService;
	
	@Autowired
	public MusicController(MusicService musicService)
	{
		this.musicService = musicService;
	}
	
	@PostMapping("")
	public ResponseEntity<?> post
	(
			@RequestAttribute("ACCESS_TOKEN") String token, 
			@Context HttpServletRequest request,
			@RequestParam String title,
			@RequestParam String lyrics,
			@RequestParam String description,
			@RequestParam MultipartFile file
	)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		MusicUploadRequest uploadRequest = new MusicUploadRequest(token, title, description, lyrics, file);
		return ResponseEntity.ok(musicService.post(uploadRequest));
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> update
	(
			@RequestAttribute("ACCESS_TOKEN") String token, 
			@Context HttpServletRequest request,
			@PathVariable("id") Long musicID,
			@RequestParam String title,
			@RequestParam String lyrics,
			@RequestParam String description
	)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		MusicUpdateRequest uploadRequest = new MusicUpdateRequest(musicID, token, title, description, lyrics);
		return ResponseEntity.ok(musicService.update(uploadRequest));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete
	(
			@RequestAttribute("ACCESS_TOKEN") String token, 
			@Context HttpServletRequest request,
			@PathVariable("id") Long musicID
	)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		MusicDeleteRequest deleteRequest = new MusicDeleteRequest(musicID, token);
		return ResponseEntity.ok(musicService.delete(deleteRequest));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> read
	(
			@RequestAttribute("ACCESS_TOKEN") String token, 
			@Context HttpServletRequest request,
			@PathVariable("id") Long musicID,
			@RequestParam(required = false, defaultValue = "false") boolean isMine
	)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		MusicReadRequest readRequest = new MusicReadRequest(musicID, token, isMine);
		return ResponseEntity.ok(musicService.read(readRequest));
	}
	
	@GetMapping("")
	public ResponseEntity<?> list
	(
			@RequestAttribute("ACCESS_TOKEN") String token, 
			@Context HttpServletRequest request,
			@RequestParam("id") String accountID
	)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		MusicListRequest readRequest = new MusicListRequest(accountID, token);
		return ResponseEntity.ok(musicService.list(readRequest));
	}
}
