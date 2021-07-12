package com.github.rlaehd62.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.github.rlaehd62.vo.Public;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Board extends BaseEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long ID;
	
	@ManyToOne(targetEntity = Account.class)
	@NotNull 
	private Account account;
	
	@NotNull 
	@NonNull
	@Column(length = 2200)
	private String context;
	
	@NotNull 
	@Enumerated(EnumType.STRING)
	private Public isPublic;
	
	
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
	private List<BoardFile> list;
	
	
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
	private List<Comment> comments;
}
