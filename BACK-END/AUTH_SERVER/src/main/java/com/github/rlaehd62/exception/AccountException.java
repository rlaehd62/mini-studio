package com.github.rlaehd62.exception;

import lombok.Getter;

@Getter
public class AccountException extends RuntimeException
{
	private AccountError error;
	
	public AccountException(AccountError error)
	{
		super(error.getMessage());
		this.error = error;
	}
}
