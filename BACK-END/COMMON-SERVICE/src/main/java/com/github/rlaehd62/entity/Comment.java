package com.github.rlaehd62.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long ID;
	
	@NotNull 
	@ManyToOne(targetEntity = Board.class)
	@NonNull 
	private Board board;
	
	@NotNull 
	@ManyToOne(targetEntity = Account.class)
	@NonNull 
	private Account account;
	
	@NotNull 
	@Column(length = 1000)
	@NonNull 
	private String context;
}
