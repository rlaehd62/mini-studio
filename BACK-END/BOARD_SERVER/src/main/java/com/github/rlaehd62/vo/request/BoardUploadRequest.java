package com.github.rlaehd62.vo.request;

import com.github.rlaehd62.entity.Board;
import com.github.rlaehd62.vo.Public;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BoardUploadRequest
{
	private String token;
	private String context;
	private Public isPublic;
	
	public Board toBoard()
	{
		return Board.builder()
				.context(context)
				.isPublic(isPublic)
				.build();
	}
}
