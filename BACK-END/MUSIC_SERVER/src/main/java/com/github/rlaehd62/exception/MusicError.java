package com.github.rlaehd62.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum MusicError
{
	MUSIC_NOT_FOUND(HttpStatus.NOT_FOUND, "MUSIC NOT FOUND (곡을 찾지 못함)"),
	MUSIC_NO_UPLOADER(HttpStatus.NOT_FOUND, "MUSIC UPLOADER NOT FOUND (곡 업로더 찾지 못함)"),
	MUSIC_NOT_MINE(HttpStatus.BAD_REQUEST, "MUSIC ACCESS DENIED (곡에 대한 권한 없음)"),
	MUSIC_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "MUSIC UPLOAD FAILED (곡 음원 업로드 실패)");
	
	MusicError(HttpStatus status, String message)
	{
		this.status = status;
		this.message = message;
	}
	private final HttpStatus status;
	private String message;
}
