package com.github.rlaehd62.service;

import java.util.List;

import javax.transaction.Transactional;

import com.github.rlaehd62.request.MusicDeleteRequest;
import com.github.rlaehd62.request.MusicListRequest;
import com.github.rlaehd62.request.MusicReadRequest;
import com.github.rlaehd62.request.MusicUpdateRequest;
import com.github.rlaehd62.request.MusicUploadRequest;
import com.github.rlaehd62.vo.music.MusicVO;

public interface MusicService
{
	@Transactional Long post(MusicUploadRequest request);
	@Transactional public MusicVO update(MusicUpdateRequest request);
	@Transactional public boolean delete(MusicDeleteRequest request);
	
	public MusicVO read(MusicReadRequest request);
	public List<MusicVO> list(MusicListRequest request);
}