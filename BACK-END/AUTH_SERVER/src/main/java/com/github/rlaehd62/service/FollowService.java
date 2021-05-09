package com.github.rlaehd62.service;

import com.github.rlaehd62.vo.follow.FollowListVO;
import com.github.rlaehd62.vo.request.follow.FollowListRequest;
import com.github.rlaehd62.vo.request.follow.FollowToggleEvent;

public interface FollowService
{
	public void toggle(FollowToggleEvent event);
	public FollowListVO getFollowList(FollowListRequest event);
}
