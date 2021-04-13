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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.rlaehd62.service.BoardService;
import com.github.rlaehd62.service.Impl.DefaultBoardService;
import com.github.rlaehd62.service.Impl.Util;
import com.github.rlaehd62.vo.BoardInfo;
import com.github.rlaehd62.vo.request.BoardDeleteRequest;
import com.github.rlaehd62.vo.request.BoardListRequest;
import com.github.rlaehd62.vo.request.BoardUpdateRequest;
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
	
	// TODO: 추후 리소스 서버 만들어지면 리소스 업로드 기능 추가하기
	
	@PostMapping("")
	public ResponseEntity<?> upload(@RequestAttribute("ACCESS_TOKEN") String token, BoardInfo info, @Context HttpServletRequest request)
	{
		if(Objects.isNull(token)) return util.makeResponseEntity(HttpStatus.NOT_FOUND, "액세스 토큰이 없습니다.");
		
		BoardUploadRequest boardUploadRequest = new BoardUploadRequest(token, info);
		return util.makeResponseEntity(HttpStatus.OK, service.upload(boardUploadRequest));
	}
	
	@PatchMapping("/{boardID}")
	public ResponseEntity<?> update(@RequestAttribute("ACCESS_TOKEN") String token, @PathVariable Long boardID, @RequestParam String context, @Context HttpServletRequest request)
	{
		if(Objects.isNull(token)) return util.makeResponseEntity(HttpStatus.NOT_FOUND, "액세스 토큰이 없습니다.");
		BoardUpdateRequest boardRequest = new BoardUpdateRequest(boardID, token, context);
		service.update(boardRequest);
		return util.makeResponseEntity(HttpStatus.OK, "게시물 No." + boardID + "를 성공적으로 업데이트 했습니다.");
	}
	
	@DeleteMapping("/{boardID}")
	public ResponseEntity<?> delete(@RequestAttribute("ACCESS_TOKEN") String token, @PathVariable Long boardID, @Context HttpServletRequest request)
	{
		if(Objects.isNull(token)) return util.makeResponseEntity(HttpStatus.NOT_FOUND, "액세스 토큰이 없습니다.");
		BoardDeleteRequest boardRequest = new BoardDeleteRequest(boardID, token);
		service.delete(boardRequest);
		return util.makeResponseEntity(HttpStatus.OK, "게시물 No." + boardID + "를 성공적으로 삭제 했습니다.");
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
			@RequestParam (required = false, defaultValue = "") String keyword,
			@Context HttpServletRequest request,
			Model model
	)
	{
		BoardListRequest boardRequest = new BoardListRequest(id, keyword, pageable);
		model.addAttribute("pageable", pageable);
		return ResponseEntity.ok(service.list(boardRequest));
	}
	
}
