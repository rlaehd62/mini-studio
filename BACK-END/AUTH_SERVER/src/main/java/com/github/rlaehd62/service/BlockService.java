package com.github.rlaehd62.service;

import com.github.rlaehd62.vo.BlackListVO;
import com.github.rlaehd62.vo.request.BlockUserListRequest;
import com.github.rlaehd62.vo.request.BlockUserToggleEvent;

public interface BlockService
{
	public void toggle(BlockUserToggleEvent event);
	public BlackListVO getBlackList(BlockUserListRequest event);
}
