package com.github.rlaehd62.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.rlaehd62.entity.Board;
import com.github.rlaehd62.entity.BoardFile;
import com.github.rlaehd62.entity.File;
import com.github.rlaehd62.entity.repository.BoardFileRepository;
import com.github.rlaehd62.exception.FileError;
import com.github.rlaehd62.exception.FileException;
import com.github.rlaehd62.service.BoardFileService;
import com.github.rlaehd62.service.FileService;
import com.github.rlaehd62.vo.request.BoardFileUploadRequest;
import com.github.rlaehd62.vo.request.BoardRequest;
import com.github.rlaehd62.vo.request.FileUploadRequest;

@Service
public class DefaultBoardFileService implements BoardFileService
{
	private Util util;
	private FileService fileService;
	private BoardFileRepository boardRepository;
	
	@Autowired
	public DefaultBoardFileService(Util util, FileService fileService, BoardFileRepository boardRepository)
	{
		this.util = util;
		this.fileService = fileService;
		this.boardRepository = boardRepository;
	}
	
	@Override
	public void upload(BoardFileUploadRequest request)
	{
		Long ID = request.getBoardID();
		String TOKEN = request.getToken();
		
		Optional<Board> optional = util.findBoard(new BoardRequest(ID, TOKEN));
		optional.orElseThrow(() -> new FileException(FileError.FILE_NO_REFERENCE));
		
		Board board = optional.get();
		Function<File, BoardFile> func = (tempFile) -> { return new BoardFile(board, tempFile); };
		
		List<MultipartFile> files = request.getFiles();
		for(MultipartFile file : files)
		{
			try
			{
				FileUploadRequest uploadRequest = new FileUploadRequest(file);
				BoardFile boardFile = fileService.uploadFile(uploadRequest, func);
				boardRepository.saveAndFlush(boardFile);
			} catch (Exception e)
			{ throw new FileException(FileError.FILE_UPLOAD_FAILURE); }
		}
		
	}

}
