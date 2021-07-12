package com.github.rlaehd62.entity;

import com.github.rlaehd62.vo.MusicGenre;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Favor
{
	@Id @GeneratedValue(strategy = GenerationType.AUTO) private Long ID;

	@NotNull
	@NonNull
	@ManyToOne(targetEntity = Account.class)
	private Account account;
	
	@NotNull 
	@Enumerated(EnumType.STRING)
	private MusicGenre genre;
}
