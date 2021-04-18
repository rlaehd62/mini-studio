package com.github.rlaehd62.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.entity.Board;
import com.github.rlaehd62.entity.repository.BoardRepository;
import com.github.rlaehd62.exception.BoardError;
import com.github.rlaehd62.exception.BoardException;
import com.github.rlaehd62.service.BoardService;
import com.github.rlaehd62.service.Util;
import com.github.rlaehd62.vo.BoardInfo;
import com.github.rlaehd62.vo.request.BoardDeleteRequest;
import com.github.rlaehd62.vo.request.BoardListRequest;
import com.github.rlaehd62.vo.request.BoardRequest;
import com.github.rlaehd62.vo.request.BoardUpdateRequest;
import com.github.rlaehd62.vo.request.BoardUploadRequest;

@Service
public class DefaultBoardService implements BoardService
{
	private Util util;
	private BoardRepository boardRepository;
	
	@Autowired
	public DefaultBoardService(Util util, BoardRepository boardRepository)
	{
		this.util = util;
		this.boardRepository = boardRepository;
	}
	
	@Override
	public Long upload(BoardUploadRequest request)
	{
		Account account = util.findAccount(request.getToken());
		
		Board board = request.toBoard();
		board.setAccount(account);
		
		boardRepository.saveAndFlush(board);
		return board.getID();
	}

	@Override
	public void update(BoardUpdateRequest request)
	{
		Long ID = request.getID();
		
		Optional<Board> boardOptional = boardRepository.findById(ID);
		boardOptional.orElseThrow(() -> new BoardException(BoardError.BOARD_NOT_FOUND));
		
		Board board = boardOptional.get();
		if(!util.isMine(board, request.getToken())) throw new BoardException(BoardError.BOARD_NOT_MINE);

		board.setContext(request.getContext());
		boardRepository.save(board);
	}
	
	@Override
	public BoardInfo get(BoardRequest request)
	{
		Long ID = request.getID();
		String TOKEN = request.getToken();
		boolean isMine = request.isMine();
		
		Optional<Board> boardOptional = boardRepository.findById(ID);
		boardOptional.orElseThrow(() -> new BoardException(BoardError.BOARD_NOT_FOUND));
		
		Board board = boardOptional.get();
		Account account = board.getAccount();
		if(isMine && !util.isMine(board, TOKEN)) throw new BoardException(BoardError.BOARD_NOT_MINE);
		
		BoardInfo info = new BoardInfo(board.getID(), board.getContext(), account.getId(), account.getUsername());
		return info;
	}

	@Override
	public List<BoardInfo> list(BoardListRequest request)
	{
		String ID = request.getAccountID();
		String KEYWORD = request.getKeyword();
		Pageable pageable = request.getPageable();
		return boardRepository.findAllByAccount_idAndContextContaining(ID, KEYWORD, pageable).stream()
				.map(value -> new BoardInfo(value.getID(), value.getContext(), value.getAccount().getId(), value.getAccount().getUsername()))
				.collect(Collectors.toList());
	}

	@Override
	public void delete(BoardDeleteRequest request)
	{
		Long ID = request.getID();
		
		Optional<Board> boardOptional = boardRepository.findById(ID);
		boardOptional.orElseThrow(() -> new BoardException(BoardError.BOARD_NOT_FOUND));
		
		Board board = boardOptional.get();
		if(!util.isMine(board, request.getToken())) throw new BoardException(BoardError.BOARD_NOT_MINE);
		
		boardRepository.delete(board);
	}
}
