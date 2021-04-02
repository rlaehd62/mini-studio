package com.github.rlaehd62.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class AccountDetails implements UserDetails
{
	private String id;
	private String pw;
	private String account_username;
	private List<SimpleGrantedAuthority> authorities;
	
	public AccountDetails(String id, String pw, String username, List<String> authorities)
	{
		setId(id);
		setPw(pw);
		setAccount_username(username);
		setAuthorities(authorities.stream()
				.map(str -> new SimpleGrantedAuthority(str))
				.collect(Collectors.toList()));
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return authorities;
	}

	@Override
	public String getPassword()
	{
		return pw;
	}

	@Override
	public String getUsername()
	{
		return id;
	}

	@Override
	public boolean isAccountNonExpired()
	{
		return false;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return false;
	}

	@Override
	public boolean isEnabled()
	{
		return false;
	}

}
