package com.github.rlaehd62.entity.file;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

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
public class File
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long ID;
	
	@NotNull 
	@Column(length = 1000)
	@NonNull 
	private String name;
	
	@NotNull 
	@Column(length = 1000)
	@NonNull 
	private String path;
}
