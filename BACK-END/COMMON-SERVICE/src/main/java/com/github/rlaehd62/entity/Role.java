package com.github.rlaehd62.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role
{
	@Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
	
	@NotNull 
	@ManyToOne(targetEntity = Account.class) 
	private Account account;
	
	@NotNull 
	private String role;
	
	public Role(String role, Account account)
	{
		this.role = role;
		this.account = account;
	}
}
