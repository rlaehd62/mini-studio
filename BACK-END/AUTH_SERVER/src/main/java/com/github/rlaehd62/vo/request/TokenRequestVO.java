package com.github.rlaehd62.vo.request;

import com.github.rlaehd62.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequestVO
{
	private String header;
	private int expiration;
	private Account account;
}
