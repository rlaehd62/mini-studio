package com.github.rlaehd62.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum FileError
{
	FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "NO FILE FOUND (파일을 찾을 수 없음)"),
	FILE_UPLOAD_FAILURE(HttpStatus.CONFLICT, "FAILED TO UPLOAD THE FILES (파일 업로드에 실패)"),
	FILE_NO_REFERENCE(HttpStatus.NOT_FOUND, "COULDN'T FIND A REFERENCE FOR FILES (참조 데이터 누락으로 업로드 실패)"),
	FILE_STREAM_FAILURE(HttpStatus.CONFLICT, "FAILED TO STREAM THE REQUESTED RESOURCE (파일 스트리밍 실패)");
	
	FileError(HttpStatus status, String message)
	{
		this.status = status;
		this.message = message;
	}
	
	private final HttpStatus status;
	private String message;
}
