package com.github.rlaehd62.vo.request;

import com.github.rlaehd62.vo.AccountVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequestVO
{
	private String header;
	private int expiration;
	private AccountVO accountVO;
}
