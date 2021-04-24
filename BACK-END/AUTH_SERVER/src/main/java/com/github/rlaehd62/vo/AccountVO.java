package com.github.rlaehd62.vo;

import java.io.Serializable;
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
public class AccountVO implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String pw;
	private String username;
	private List<String> roles;
	
	public AccountVO(Claims claims)
	{
		setId(claims.getSubject());
		setUsername(claims.get("username").toString());
		setRoles((List<String>) claims.get("authorities"));
	}
	
	public AccountVO(Account account)
	{
		setId(account.getId());
		setUsername(account.getUsername());
		
		List<Role> roleList = account.getRoles();
		List<String> roles = roleList.stream()
				.map(value -> value.getRole())
				.collect(Collectors.toList());
		setRoles(roles);
	}
}
