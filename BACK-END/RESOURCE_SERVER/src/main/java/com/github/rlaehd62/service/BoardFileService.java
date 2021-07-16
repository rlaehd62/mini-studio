package com.github.rlaehd62.service;

import com.github.rlaehd62.vo.resource.board.BoardDeleteRequest;
import com.github.rlaehd62.vo.resource.board.BoardFileUploadRequest;
import com.github.rlaehd62.vo.resource.board.BoardListVO;
import com.github.rlaehd62.vo.resource.board.BoardRequest;

public interface BoardFileService
{
	public void upload(BoardFileUploadRequest request);
	public BoardListVO get(BoardRequest request);
	public void delete(BoardDeleteRequest request);
}
