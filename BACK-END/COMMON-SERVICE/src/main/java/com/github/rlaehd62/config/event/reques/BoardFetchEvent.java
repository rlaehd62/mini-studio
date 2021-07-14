package com.github.rlaehd62.config.event.reques;

import com.github.rlaehd62.entity.board.Board;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BoardFetchEvent
{
	@NonNull private Long ID;
	@NonNull private String token;
	private boolean isMine = false;
	private boolean successful = false;
	private Board result;
}
