package com.github.rlaehd62.vo.resource.board;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardDeleteRequest
{
	private Long boardFileID;
	private String token;
}
