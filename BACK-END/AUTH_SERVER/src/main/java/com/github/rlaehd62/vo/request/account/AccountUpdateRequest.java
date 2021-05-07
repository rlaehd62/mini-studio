package com.github.rlaehd62.vo.request.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class AccountUpdateRequest
{
	@NonNull private String token;
	private String pw;
	private String email;
	private String username;
}
