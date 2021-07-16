package com.github.rlaehd62.exception;

import lombok.Getter;

@Getter
public class MusicException extends RuntimeException
{
	private MusicError error;
	
	public MusicException(MusicError error)
	{
		super(error.getMessage());
		this.error = error;
	}
}
