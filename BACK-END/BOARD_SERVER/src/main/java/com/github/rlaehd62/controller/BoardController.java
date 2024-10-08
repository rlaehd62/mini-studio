package com.github.rlaehd62.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.github.rlaehd62.vo.request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.rlaehd62.exception.TokenError;
import com.github.rlaehd62.exception.TokenException;
import com.github.rlaehd62.service.BoardService;
import com.github.rlaehd62.service.Util;
import com.github.rlaehd62.service.Impl.DefaultBoardService;
import com.github.rlaehd62.vo.Public;
import com.github.rlaehd62.vo.board.BoardInfo;

@RestController
@RequestMapping("/boards")
public class BoardController
{
	private com.github.rlaehd62.service.Util util;
	private BoardService service;
	
	@Autowired
	public BoardController(Util util, DefaultBoardService service)
	{
		this.util = util;
		this.service = service;
	}
	
	@PostMapping("")
	public ResponseEntity<?> upload
	(
			@RequestAttribute("ACCESS_TOKEN") String token, 
			@RequestParam(required = false, defaultValue = "YES") Public isPublic,
			@RequestParam String context,
			@Context HttpServletRequest request
	)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		BoardUploadRequest boardUploadRequest = new BoardUploadRequest(token, context, isPublic);
		return util.makeResponseEntity(HttpStatus.OK, service.upload(boardUploadRequest));
	}
	
	@PatchMapping("/{boardID}")
	public ResponseEntity<?> update
	(
			@RequestAttribute("ACCESS_TOKEN") String token, 
			@PathVariable Long boardID,
			BoardInfo info,
			@Context HttpServletRequest request
	)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		BoardUpdateRequest boardRequest = new BoardUpdateRequest(boardID, token, info.getContext(), info.getIsPublic());
		service.update(boardRequest);
		return ResponseEntity.ok(service.update(boardRequest));
	}
	
	@DeleteMapping("/{boardID}")
	public ResponseEntity<?> delete(@RequestAttribute("ACCESS_TOKEN") String token, @PathVariable Long boardID, @Context HttpServletRequest request)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		BoardDeleteRequest boardRequest = new BoardDeleteRequest(boardID, token);
		service.delete(boardRequest);
		return util.makeResponseEntity(HttpStatus.OK, "게시물 No." + boardID + "를 성공적으로 삭제 했습니다.");
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getBoard
	(
			@RequestAttribute("ACCESS_TOKEN") String token, 
			@RequestParam(required = false, defaultValue = "false") boolean isMine,
			@PathVariable("id") Long boardID, 
			@Context HttpServletRequest request
	)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		BoardRequest boardRequest = new BoardRequest(boardID, token, isMine);
		return util.makeResponseEntity(HttpStatus.OK, service.get(boardRequest));
	}	
	
	@GetMapping("")
	ResponseEntity<?> getBoards
	(
			@RequestAttribute("ACCESS_TOKEN") String token,
			@PageableDefault(sort = "ID", direction = Direction.DESC) Pageable pageable,
			@RequestParam String id, 
			@RequestParam (required = false, defaultValue = "") String keyword,
			@Context HttpServletRequest request,
			Model model
	)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		BoardListRequest boardRequest = new BoardListRequest(id, pageable, keyword, token);
		model.addAttribute("pageable", pageable);
		return ResponseEntity.ok(service.list(boardRequest));
	}

	@GetMapping("/follows")
	ResponseEntity<?> getFollowBoards
			(
					@RequestAttribute("ACCESS_TOKEN") String token,
					@PageableDefault(sort = "ID", direction = Direction.DESC) Pageable pageable,
					@RequestParam (required = false, defaultValue = "") String keyword,
					@Context HttpServletRequest request,
					Model model
			)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		BoardFollowListRequest followListRequest = new BoardFollowListRequest(token, keyword, pageable);
		model.addAttribute("pageable", pageable);
		return ResponseEntity.ok(service.search(followListRequest));
	}
	
}
