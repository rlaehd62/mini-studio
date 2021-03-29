package com.github.rlaehd62.vo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestVO
{
	private HttpServletRequest request;
	private HttpServletResponse response;
}
