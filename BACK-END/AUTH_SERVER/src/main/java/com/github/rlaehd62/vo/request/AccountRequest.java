package com.github.rlaehd62.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest
{
	private String id;
	private String token;
	private boolean isMine;
}
