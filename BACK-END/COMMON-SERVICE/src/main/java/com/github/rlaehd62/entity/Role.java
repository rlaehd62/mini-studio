package com.github.rlaehd62.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@JsonIgnore @ManyToOne(targetEntity = Account.class) private Account account;
	private String role;
	
	public Role(String role, Account account)
	{
		this.role = role;
		this.account = account;
	}
}
