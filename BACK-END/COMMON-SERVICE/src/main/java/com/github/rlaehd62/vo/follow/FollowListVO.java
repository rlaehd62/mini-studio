package com.github.rlaehd62.vo.follow;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowListVO implements Serializable
{
	private static final long serialVersionUID = 1L;
	private List<FollowVO> follows;
}
