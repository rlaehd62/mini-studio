package com.github.rlaehd62.service;

import com.github.rlaehd62.vo.block.BlackListVO;
import com.github.rlaehd62.vo.request.block.BlockUserListRequest;
import com.github.rlaehd62.vo.request.block.BlockUserToggleEvent;

public interface BlockService
{
	public void toggle(BlockUserToggleEvent event);
	public BlackListVO getBlackList(BlockUserListRequest event);
}
