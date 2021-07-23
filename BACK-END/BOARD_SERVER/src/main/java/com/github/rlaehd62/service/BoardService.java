package com.github.rlaehd62.service;

import java.util.Map;

import javax.transaction.Transactional;

import com.github.rlaehd62.vo.board.BoardVO;
import com.github.rlaehd62.vo.request.*;

public interface BoardService
{
	@Transactional public Long upload(BoardUploadRequest request);
	@Transactional public BoardVO update(BoardUpdateRequest request);
	@Transactional public void delete(BoardDeleteRequest request);
	
	public BoardVO get(BoardRequest request);
	public Map<String, Object> list(BoardListRequest request);
	public Map<String, Object> search(BoardFollowListRequest request);
}
