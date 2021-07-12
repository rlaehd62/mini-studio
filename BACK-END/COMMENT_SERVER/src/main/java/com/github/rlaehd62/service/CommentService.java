package com.github.rlaehd62.service;

import java.util.Map;

import javax.transaction.Transactional;

import com.github.rlaehd62.vo.comment.CommentVO;
import com.github.rlaehd62.vo.request.CommentDeleteRequest;
import com.github.rlaehd62.vo.request.CommentListRequest;
import com.github.rlaehd62.vo.request.CommentPatchRequest;
import com.github.rlaehd62.vo.request.CommentReadRequest;
import com.github.rlaehd62.vo.request.CommentUploadRequest;

public interface CommentService
{
	@Transactional Long upload(CommentUploadRequest request);
	@Transactional public CommentVO update(CommentPatchRequest request);
	@Transactional public void delete(CommentDeleteRequest request);
	
	public CommentVO get(CommentReadRequest request);
	public Map<String, Object> list(CommentListRequest request);
}
