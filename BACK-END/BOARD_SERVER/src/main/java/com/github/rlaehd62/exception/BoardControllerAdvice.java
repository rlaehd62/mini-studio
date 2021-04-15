package com.github.rlaehd62.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.rlaehd62.controller.BoardController;
import com.github.rlaehd62.vo.ErrorVO;

@RestControllerAdvice(basePackageClasses = BoardController.class)
public class BoardControllerAdvice
{
	@ExceptionHandler(BoardException.class)
	public ResponseEntity<ErrorVO> handleBoard(final BoardException e)
	{
		BoardError be = e.getError();
		return ResponseEntity
				.status(be.getStatus())
				.body
				(
						ErrorVO.builder()
						.status(be.getStatus())
						.errorMessage(be.getMessage())
						.build()
				);
	}
	
	@ExceptionHandler(TokenException.class)
	public ResponseEntity<ErrorVO> handleToken(final TokenException e)
	{
		TokenError te = e.getError();
		return ResponseEntity
				.status(te.getStatus())
				.body
				(
						ErrorVO.builder()
						.status(te.getStatus())
						.errorMessage(te.getMessage())
						.build()
				);
	}
}
