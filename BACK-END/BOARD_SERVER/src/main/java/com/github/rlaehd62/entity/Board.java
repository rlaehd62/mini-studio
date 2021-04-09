package com.github.rlaehd62.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Board
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long ID;
	
	@ManyToOne(targetEntity = Account.class)
	private Account account;
	
	@NonNull
	@Column(length = 2200)
	private String context;
}
