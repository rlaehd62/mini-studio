package com.github.rlaehd62.vo.request;

import com.github.rlaehd62.entity.Board;
import com.github.rlaehd62.vo.BoardInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BoardUploadRequest
{
	private String token;
	private BoardInfo boardInfo;
	
	public Board toBoard()
	{
		return Board.builder()
				.context(boardInfo.getContext())
				.isPublic(boardInfo.getIsPublic())
				.build();
	}
}
