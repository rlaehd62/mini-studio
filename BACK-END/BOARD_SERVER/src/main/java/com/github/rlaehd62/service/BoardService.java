package com.github.rlaehd62.service;

import java.util.List;

import javax.transaction.Transactional;

import com.github.rlaehd62.vo.BoardVO;
import com.github.rlaehd62.vo.request.BoardDeleteRequest;
import com.github.rlaehd62.vo.request.BoardListRequest;
import com.github.rlaehd62.vo.request.BoardRequest;
import com.github.rlaehd62.vo.request.BoardUpdateRequest;
import com.github.rlaehd62.vo.request.BoardUploadRequest;

public interface BoardService
{
	@Transactional public Long upload(BoardUploadRequest request);
	@Transactional public void update(BoardUpdateRequest request);
	@Transactional public void delete(BoardDeleteRequest request);
	
	public BoardVO get(BoardRequest request);
	public List<BoardVO> list(BoardListRequest request);
}
