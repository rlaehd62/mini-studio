package com.github.rlaehd62.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateRequest
{
	private String id;
	private String pw;
	private String email;
	private String username;
}
