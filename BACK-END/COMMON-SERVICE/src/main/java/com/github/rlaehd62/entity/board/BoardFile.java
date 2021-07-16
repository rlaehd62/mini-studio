package com.github.rlaehd62.entity.board;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.github.rlaehd62.entity.file.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class BoardFile
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long ID;
	
	@NotNull 
	@NonNull
	@ManyToOne(targetEntity = Board.class)
	private Board board;
	
	@NotNull 
	@NonNull
	@OneToOne(targetEntity = File.class, cascade = CascadeType.ALL)
	private File file;
}
