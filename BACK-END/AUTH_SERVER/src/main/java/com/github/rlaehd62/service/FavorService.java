package com.github.rlaehd62.service;

import com.github.rlaehd62.vo.favor.FavorListVO;
import com.github.rlaehd62.vo.follow.FollowListVO;
import com.github.rlaehd62.vo.request.favor.FavorListRequest;
import com.github.rlaehd62.vo.request.favor.FavorToggleEvent;
import com.github.rlaehd62.vo.request.follow.FollowListRequest;
import com.github.rlaehd62.vo.request.follow.FollowToggleEvent;

public interface FavorService
{
	public void toggle(FavorToggleEvent event);
	public FavorListVO getFavorList(FavorListRequest event);
}
