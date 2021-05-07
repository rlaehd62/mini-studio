package com.github.rlaehd62.vo.follow;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FollowListVO
{
	private List<FollowVO> follows;
}
