package com.github.rlaehd62.exception;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.rlaehd62.controller.FileController;
import com.github.rlaehd62.vo.ErrorVO;

@RestControllerAdvice(basePackageClasses = FileController.class)
public class FileControllerAdvice
{
	@ExceptionHandler(FileException.class)
	public ResponseEntity<ErrorVO> handleFileError(final FileException e)
	{
		FileError error = e.getError();
		return ResponseEntity
				.status(error.getStatus())
				.body
				(
						ErrorVO.builder()
						.status(error.getStatus())
						.errorMessage(error.getMessage())
						.build()
				);
	}
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<ErrorVO> handleIOError(final IOException e)
	{
		FileError error = FileError.FILE_STREAM_FAILURE;
		return ResponseEntity
				.status(error.getStatus())
				.body
				(
						ErrorVO.builder()
						.status(error.getStatus())
						.errorMessage(error.getMessage())
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
