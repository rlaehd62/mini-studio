package com.github.rlaehd62.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.github.rlaehd62.vo.account.AccountCreateRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Account
{
	@NonNull @Id private String id;
	private String pw;
	private String username;
	private String email;
	
	@OneToMany(mappedBy = "account", fetch = FetchType.EAGER ,cascade = CascadeType.ALL)
	private List<Role> roles = new ArrayList<>();
	
	@OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
	private List<Follow> follows = new ArrayList<>();
	
	@OneToMany(mappedBy = "followee", cascade = CascadeType.ALL)
	private List<Follow> followees = new ArrayList<>();
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<Board> boards = new ArrayList<>();
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<Block> blocks = new ArrayList<>();
	
	@OneToMany(mappedBy = "blocked", cascade = CascadeType.ALL)
	private List<Block> blocked = new ArrayList<>();
	
	
	public Account(String id, String pw, String username)
	{
		this.id = id;
		this.pw = pw;
		this.username = username;
		this.roles = new ArrayList<>();
		addRole("ROLE_USER");
	}
	
	public Account(String id, String pw, String username, String email)
	{
		this.id = id;
		this.pw = pw;
		this.username = username;
		this.email = email;
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
