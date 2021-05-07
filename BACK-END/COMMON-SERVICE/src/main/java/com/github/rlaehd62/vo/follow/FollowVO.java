package com.github.rlaehd62.vo.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FollowVO
{
	private String targetID;
	private String targetUsername;
}
