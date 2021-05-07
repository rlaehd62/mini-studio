package com.github.rlaehd62.vo.request.follow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowToggleEvent
{
	private String token;
	private String targetID;
}
