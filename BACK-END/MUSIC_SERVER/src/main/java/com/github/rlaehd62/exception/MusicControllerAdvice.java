package com.github.rlaehd62.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.rlaehd62.controller.MusicController;
import com.github.rlaehd62.vo.ErrorVO;

@RestControllerAdvice(basePackageClasses = MusicController.class)
public class MusicControllerAdvice
{
	@ExceptionHandler(MusicException.class)
	public ResponseEntity<ErrorVO> handleBoard(final MusicException e)
	{
		MusicError be = e.getError();
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
}
