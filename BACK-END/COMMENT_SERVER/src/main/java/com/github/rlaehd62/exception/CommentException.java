package com.github.rlaehd62.exception;

import lombok.Getter;

@Getter
public class CommentException extends RuntimeException
{
	private CommentError error;
	
	public CommentException(CommentError error)
	{
		super(error.getMessage());
		this.error = error;
	}
}
