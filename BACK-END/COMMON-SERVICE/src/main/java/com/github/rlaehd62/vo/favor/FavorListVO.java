package com.github.rlaehd62.vo.favor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class FavorListVO
{
	private List<FavorVO> follows;
}
