package com.github.rlaehd62.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.rlaehd62.exception.TokenError;
import com.github.rlaehd62.exception.TokenException;
import com.github.rlaehd62.service.BoardFileService;
import com.github.rlaehd62.service.FileService;
import com.github.rlaehd62.service.Impl.DefaultBoardFileService;
import com.github.rlaehd62.service.Impl.DefaultFileService;
import com.github.rlaehd62.vo.resource.FileRequest;
import com.github.rlaehd62.vo.resource.board.BoardDeleteRequest;
import com.github.rlaehd62.vo.resource.board.BoardFileUploadRequest;
import com.github.rlaehd62.vo.resource.board.BoardListVO;
import com.github.rlaehd62.vo.resource.board.BoardRequest;

@RestController
@RequestMapping("/files")
public class FileController
{
	private FileService fileService;
	private BoardFileService boardFileService;
	
	@Autowired
	public FileController(DefaultFileService fileService,DefaultBoardFileService boardFileService)
	{
		this.fileService = fileService;
		this.boardFileService = boardFileService;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResourceRegion> stream(@PathVariable("id") Long fileID,  @RequestHeader HttpHeaders headers) throws IOException
	{
		FileRequest request = new FileRequest(fileID, headers);
		ResourceRegion region = fileService.stream(request);
		
		return ResponseEntity
				.status(HttpStatus.PARTIAL_CONTENT)
				.contentType(MediaTypeFactory.getMediaType(region.getResource())
				.orElse(MediaType.APPLICATION_OCTET_STREAM))
				.body(region);
	}
	
	@PostMapping("/board/{id}")
	public ResponseEntity<?> upload(@RequestAttribute("ACCESS_TOKEN") String token, @PathVariable Long id, @RequestParam List<MultipartFile> files)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		BoardFileUploadRequest request = new BoardFileUploadRequest(id, token, files);
		boardFileService.upload(request);
		return ResponseEntity.ok("파일을 성공적으로 업로드 햇습니다!");
	}
	
	@DeleteMapping("/board/{id}")
	public ResponseEntity<?> delete(@RequestAttribute("ACCESS_TOKEN") String token, @PathVariable("id") Long boardFileID)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		BoardDeleteRequest request = new BoardDeleteRequest(boardFileID, token);
		boardFileService.delete(request);
		return ResponseEntity.ok("파일을 성공적으로 삭제 했습니다.");
	}
	
	@GetMapping("/board/{id}")
	public ResponseEntity<?> get(@PathVariable("id") Long boardID)
	{
		BoardListVO vo = boardFileService.get(new BoardRequest(boardID));
		return ResponseEntity.ok(vo);
	}
}
