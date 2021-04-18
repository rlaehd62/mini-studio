package com.github.rlaehd62.service;

import com.github.rlaehd62.vo.BoardListVO;
import com.github.rlaehd62.vo.request.BoardFileUploadRequest;
import com.github.rlaehd62.vo.request.BoardRequest;

public interface BoardFileService
{
	public void upload(BoardFileUploadRequest request);
	public BoardListVO get(BoardRequest request);
}
