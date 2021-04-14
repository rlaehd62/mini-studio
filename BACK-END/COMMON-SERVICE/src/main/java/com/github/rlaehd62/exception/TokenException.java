package com.github.rlaehd62.exception;

import lombok.Data;

@Data
public class TokenException extends RuntimeException
{
	private TokenError error;
	
	public TokenException(TokenError error)
	{
		super(error.getMessage());
		this.error = error;
	}
}
