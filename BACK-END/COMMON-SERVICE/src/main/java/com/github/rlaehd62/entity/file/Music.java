package com.github.rlaehd62.entity.file;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.github.rlaehd62.entity.BaseEntity;
import com.github.rlaehd62.entity.auth.Account;
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
public class Music extends BaseEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long ID;
	
	@NotNull 
	@ManyToOne(targetEntity = Account.class)
	@NonNull 
	private Account account;
	
	@NotNull 
	@OneToOne(targetEntity = File.class, cascade = CascadeType.REMOVE)
	@NonNull 
	private File file;
	
	@NotNull 
	@Column(length = 500)
	@NonNull 
	private String title;
	
	@NotNull 
	@Column(length = 2500)
	@NonNull 
	private String description;
	
	@NotNull 
	@Column(length = 3000)
	@NonNull 
	private String lyrics;
}
