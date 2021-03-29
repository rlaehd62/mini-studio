package com.github.rlaehd62.vo.request;

import javax.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest
{
	private String id;
	private String pw;
	private HttpServletRequest servletRequest;
}
