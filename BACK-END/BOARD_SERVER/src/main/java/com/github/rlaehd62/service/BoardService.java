package com.github.rlaehd62.service;

import java.util.List;

import javax.transaction.Transactional;

import com.github.rlaehd62.vo.BoardInfo;
import com.github.rlaehd62.vo.request.BoardListRequest;
import com.github.rlaehd62.vo.request.BoardUploadRequest;

public interface BoardService
{
	@Transactional public Long upload(BoardUploadRequest request);
	public BoardInfo get(long ID);
	public List<BoardInfo> list(BoardListRequest request);
}
