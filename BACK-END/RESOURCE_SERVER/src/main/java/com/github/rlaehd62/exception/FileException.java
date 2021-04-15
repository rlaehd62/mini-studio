package com.github.rlaehd62.exception;

import lombok.Data;

@Data
public class FileException extends RuntimeException
{
	private FileError error;
	
	public FileException(FileError error)
	{
		super(error.getMessage());
		this.error = error;
	}
}
