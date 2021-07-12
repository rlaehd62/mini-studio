package com.github.rlaehd62.vo.resource.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BoardRequest
{
	@NonNull Long boardID;
	String token;
}
