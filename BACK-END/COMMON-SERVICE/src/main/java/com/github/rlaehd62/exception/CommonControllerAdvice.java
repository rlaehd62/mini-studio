package com.github.rlaehd62.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.rlaehd62.vo.ErrorVO;

@RestControllerAdvice(basePackages = {"com.rlaehd62"})
public class CommonControllerAdvice
{
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorVO> handle500(final Exception e)
	{
		String msg = e.toString();
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body
				(
						ErrorVO.builder()
						.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.errorMessage("[" + msg +"] 서버 내에서 알수 없는 오류 발생")
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
