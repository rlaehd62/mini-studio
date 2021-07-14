package com.github.rlaehd62.entity.auth;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Block
{
	@Id @GeneratedValue(strategy = GenerationType.AUTO) private Long ID;
	
	@NotNull 
	@NonNull 
	@ManyToOne(targetEntity = Account.class) 
	private Account account;
	
	@NotNull 
	@NonNull 
	@ManyToOne(targetEntity = Account.class) 
	private Account blocked;
}
