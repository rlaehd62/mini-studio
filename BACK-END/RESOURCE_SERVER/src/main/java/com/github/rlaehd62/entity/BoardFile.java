package com.github.rlaehd62.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
public class BoardFile
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long ID;
	
	@ManyToOne(targetEntity = Board.class, cascade = CascadeType.PERSIST)
	private Board board;
	
	@OneToOne(targetEntity = File.class, cascade = CascadeType.ALL)
	private File file;
}
