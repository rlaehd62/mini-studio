package com.github.rlaehd62.controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.rlaehd62.service.BoardService;
import com.github.rlaehd62.service.Impl.DefaultBoardService;
import com.github.rlaehd62.service.Impl.Util;
import com.github.rlaehd62.vo.BoardInfo;
import com.github.rlaehd62.vo.TokenType;
import com.github.rlaehd62.vo.request.BoardListRequest;
import com.github.rlaehd62.vo.request.BoardUploadRequest;

@RestController
@RequestMapping("/boards")
public class BoardController
{
	private Util util;
	private BoardService service;
	
	@Autowired
	public BoardController(Util util, DefaultBoardService service)
	{
		this.util = util;
		this.service = service;
	}
	
	@PostMapping("")
	public ResponseEntity<?> upload(BoardInfo info, @Context HttpServletRequest request)
	{
		String HEADER = TokenType.ACCESS.getName();
		String token = (String) request.getAttribute(HEADER);
		if(Objects.isNull(token)) return util.makeResponseEntity(HttpStatus.NOT_FOUND, "액세스 토큰이 없습니다.");
		
		BoardUploadRequest boardUploadRequest = new BoardUploadRequest(token, info);
		return util.makeResponseEntity(HttpStatus.OK, service.upload(boardUploadRequest));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getBoard(@PathVariable("id") Long boardID, @Context HttpServletRequest request)
	{
		return util.makeResponseEntity(HttpStatus.OK, service.get(boardID));
	}	
	
	@GetMapping("")
	ResponseEntity<?> getBoards
	(
			@PageableDefault(sort = "ID", direction = Direction.DESC) Pageable pageable, 
			@RequestParam String id, 
			@Context HttpServletRequest request,
			Model model
	)
	{
		BoardListRequest boardRequest = new BoardListRequest(id, pageable);
		model.addAttribute("pageable", pageable);
		return ResponseEntity.ok(service.list(boardRequest));
	}
	
}
