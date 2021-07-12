package com.github.rlaehd62.vo.resource.board;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardListVO
{
	private List<BoardFileVO> list;
}
