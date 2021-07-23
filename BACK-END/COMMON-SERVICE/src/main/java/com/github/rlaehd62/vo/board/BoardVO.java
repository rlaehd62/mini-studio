package com.github.rlaehd62.vo.board;

import java.time.LocalDateTime;

import com.github.rlaehd62.entity.board.Board;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardVO
{
	private Long ID;
	private String context;
	private String uploaderID;
	private String uploaderUsername;
	private LocalDateTime createdDate;

	public static BoardVO toBoardVO(Board value)
	{
		return BoardVO.builder()
				.ID(value.getID())
				.context(value.getContext())
				.uploaderID(value.getAccount().getId())
				.uploaderUsername(value.getAccount().getUsername())
				.createdDate(value.getCreatedDate())
				.build();
	}
}
