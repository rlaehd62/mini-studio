package com.github.rlaehd62.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.rlaehd62.config.event.reques.AccountCheckEvent;
import com.github.rlaehd62.entity.Board;
import com.github.rlaehd62.entity.BoardFile;
import com.github.rlaehd62.entity.File;
import com.github.rlaehd62.entity.repository.BoardFileRepository;
import com.github.rlaehd62.exception.FileError;
import com.github.rlaehd62.exception.FileException;
import com.github.rlaehd62.service.BoardFileService;
import com.github.rlaehd62.service.FileService;
import com.github.rlaehd62.vo.BoardFileVO;
import com.github.rlaehd62.vo.BoardListVO;
import com.github.rlaehd62.vo.request.BoardDeleteRequest;
import com.github.rlaehd62.vo.request.BoardFileUploadRequest;
import com.github.rlaehd62.vo.request.BoardRequest;
import com.github.rlaehd62.vo.request.FileUploadRequest;
import com.google.common.eventbus.EventBus;

@Service
public class DefaultBoardFileService implements BoardFileService
{
	private EventBus eventBus;
	private BoardUtil boardUtil;
	private FileService fileService;
	private BoardFileRepository boardRepository;
	
	@Autowired
	public DefaultBoardFileService(EventBus eventBus, BoardUtil boardUtil, DefaultFileService fileService, BoardFileRepository boardRepository)
	{
		this.eventBus = eventBus;
		this.boardUtil = boardUtil;
		this.fileService = fileService;
		this.boardRepository = boardRepository;
	}
	
	@Override
	public void upload(BoardFileUploadRequest request)
	{
		Long ID = request.getBoardID();
		String TOKEN = request.getToken();
		
		Optional<Board> optional = boardUtil.findBoard(new BoardRequest(ID, TOKEN));
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

	@Override
	public BoardListVO get(BoardRequest request)
	{
		List<BoardFile> list = boardRepository.findAllByBoard_ID(request.getBoardID());
		List<BoardFileVO> voList = 
				list.stream()
				.map(boardFile -> new BoardFileVO(boardFile.getID(), boardFile.getFile().getID()))
				.collect(Collectors.toList());
		return new BoardListVO(voList);
	}

	@Override
	public void delete(BoardDeleteRequest request)
	{
		Long ID = request.getBoardFileID();
		String token = request.getToken();
		
		Optional<BoardFile> optional = boardRepository.findById(ID);
		optional.orElseThrow(() -> new FileException(FileError.FILE_NOT_FOUND));	
		
		BoardFile boardFile = optional.get();
		Board board = boardFile.getBoard();
		
		AccountCheckEvent event = AccountCheckEvent.builder()
				.token(token)
				.comparsion(board.getAccount())
				.build();
		eventBus.post(event);
		
		if(!event.isSuccessful()) throw new FileException(FileError.FILE_ACCESS_DENIED);
		boardRepository.delete(boardFile);
	}
}
