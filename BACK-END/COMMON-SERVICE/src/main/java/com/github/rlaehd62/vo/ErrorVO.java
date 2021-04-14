package com.github.rlaehd62.vo;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorVO
{
	private HttpStatus status;
	private String errorMessage;
}
