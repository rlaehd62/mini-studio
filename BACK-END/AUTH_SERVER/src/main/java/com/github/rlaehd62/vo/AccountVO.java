package com.github.rlaehd62.vo;

import java.util.List;
import java.util.stream.Collectors;

import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.entity.Role;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountVO
{
	private String id;
	private String pw;
	private String username;
	private List<String> roles;
	
	public AccountVO(Claims claims)
	{
		setId(claims.getSubject());
		setUsername(claims.get("username").toString());
		setPw("PASSWORDS ARE PROVIDED FOR NO ONE.");
		setRoles((List<String>) claims.get("authorities"));
	}
	
	public AccountVO(Account account)
	{
		this(account, false);
	}
	
	public AccountVO(Account account, boolean isAcceptable)
	{
		setId(account.getId());
		setUsername(account.getUsername());
		setPw(isAcceptable ? account.getPw() : encoder(account.getPw()));

		
		List<Role> roleList = account.getRoles();
		List<String> roles = roleList.stream()
				.map(value -> value.getRole())
				.collect(Collectors.toList());
		setRoles(roles);
	}
	
	private String encoder(String str)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < str.length(); i++) sb.append("*");
		return sb.toString();
	}
}
