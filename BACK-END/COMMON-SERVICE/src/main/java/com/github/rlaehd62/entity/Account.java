package com.github.rlaehd62.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.rlaehd62.vo.AccountCreateRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account
{
	@Id private String id;
	private String pw;
	private String username;
	private String email;
	
	@JsonIgnore
	@OneToMany(mappedBy = "account", fetch = FetchType.EAGER ,cascade = CascadeType.ALL)
	private List<Role> roles;
	
	public Account(String id, String pw, String username)
	{
		this.id = id;
		this.pw = pw;
		this.username = username;
		this.roles = new ArrayList<>();
		addRole("ROLE_USER");
	}
	
	public Account(AccountCreateRequest request)
	{
		this.id = request.getId();
		this.pw = request.getPw();
		this.username = request.getUsername();
		this.email = request.getEmail();
		this.roles = new ArrayList<>();
		addRole("ROLE_USER");
	}
	
	public void addRole(String role)
	{
		Role roleObject = new Role(role, this);
		roles.add(roleObject);
	}
}
