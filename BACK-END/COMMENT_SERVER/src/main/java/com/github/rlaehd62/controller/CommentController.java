package com.github.rlaehd62.controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import com.github.rlaehd62.service.CommentService;
import com.github.rlaehd62.service.Impl.DefaultCommentService;
import com.github.rlaehd62.vo.request.CommentDeleteRequest;
import com.github.rlaehd62.vo.request.CommentListRequest;
import com.github.rlaehd62.vo.request.CommentPatchRequest;
import com.github.rlaehd62.vo.request.CommentReadRequest;
import com.github.rlaehd62.vo.request.CommentUploadRequest;

@RestController
@RequestMapping("/comments")
public class CommentController
{
	private CommentService commentService;
	
	@Autowired
	public CommentController(DefaultCommentService commentService)
	{
		this.commentService = commentService;
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<?> post
	(
			@RequestAttribute("ACCESS_TOKEN") String token,
			@Context HttpServletRequest request,
			@PathVariable("id") Long boardID,
			@RequestParam String context
	)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		CommentUploadRequest upload = new CommentUploadRequest(boardID, token, context);
		return ResponseEntity.ok(commentService.upload(upload));
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> patch
	(
			@RequestAttribute("ACCESS_TOKEN") String token,
			@Context HttpServletRequest request,
			@PathVariable("id") Long commentID,
			@RequestParam String context
	)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		CommentPatchRequest patch = new CommentPatchRequest(commentID, token, context);
		return ResponseEntity.ok(commentService.update(patch));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete
	(
			@RequestAttribute("ACCESS_TOKEN") String token,
			@Context HttpServletRequest request,
			@PathVariable("id") Long commentID
	)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		CommentDeleteRequest delete = new CommentDeleteRequest(commentID, token);
		commentService.delete(delete);
		return ResponseEntity.ok("Successfully Deleted the Comment");
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> list
	(
			@RequestAttribute("ACCESS_TOKEN") String token,
			@Context HttpServletRequest request,
			@PathVariable("id") Long boardID,
			@PageableDefault(sort = "ID", direction = Direction.DESC) Pageable pageable,
			Model model
	)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		CommentListRequest listRequest = new CommentListRequest(boardID, token, pageable);
		return ResponseEntity.ok(commentService.list(listRequest));
	}
	
	@GetMapping("/detail/{commentID}")
	public ResponseEntity<?> get
	(
			@RequestAttribute("ACCESS_TOKEN") String token,
			@Context HttpServletRequest request,
			@PathVariable("commentID") Long commentID,
			@RequestParam(required = false, defaultValue = "false") boolean isMine,
			Model model
	)
	{
		if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
		CommentReadRequest get = new CommentReadRequest(commentID, token, isMine);
		return ResponseEntity.ok(commentService.get(get));
	}
}
