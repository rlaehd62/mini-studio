package com.github.rlaehd62.exception;

import lombok.Getter;

@Getter
public class BoardException extends RuntimeException
{
	private BoardError error;
	
	public BoardException(BoardError error)
	{
		super(error.getMessage());
		this.error = error;
	}
}
