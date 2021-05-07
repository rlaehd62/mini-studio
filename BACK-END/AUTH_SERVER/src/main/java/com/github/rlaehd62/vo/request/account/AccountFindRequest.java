package com.github.rlaehd62.vo.request.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountFindRequest
{
	private String id;
	private String email;
}
