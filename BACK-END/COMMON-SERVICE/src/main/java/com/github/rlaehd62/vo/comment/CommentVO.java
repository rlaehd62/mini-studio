package com.github.rlaehd62.vo.comment;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO
{
	private Long ID;
	private LocalDateTime date;
	private String accountID;
	private String accountUsername;
	private String context;
}
