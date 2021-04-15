package com.github.rlaehd62.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.rlaehd62.exception.TokenError;
import com.github.rlaehd62.exception.TokenException;
import com.github.rlaehd62.service.BoardFileService;
import com.github.rlaehd62.service.Impl.DefaultBoardFileService;
import com.github.rlaehd62.vo.request.BoardFileUploadRequest;

@RestController
@RequestMapping("/files")
public class FileController
{
	private BoardFileService boardFileService;
	
	@Autowired
	public FileController(DefaultBoardFileService boardFileService)
	{
		this.boardFileService = boardFileService;
	}
	
	@PostMapping("/board/{id}")
	public ResponseEntity<?> upload(@RequestAttribute("ACCESS_TOKEN") String token, @PathVariable Long id, @RequestParam List<MultipartFile> files)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		BoardFileUploadRequest request = new BoardFileUploadRequest(id, token, files);
		boardFileService.upload(request);
		return ResponseEntity.ok("파일을 성공적으로 업로드 햇습니다!");
	}
}
