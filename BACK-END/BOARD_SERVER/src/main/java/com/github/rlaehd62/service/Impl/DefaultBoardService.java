package com.github.rlaehd62.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.entity.Board;
import com.github.rlaehd62.entity.repository.BoardRepository;
import com.github.rlaehd62.service.BoardService;
import com.github.rlaehd62.vo.BoardInfo;
import com.github.rlaehd62.vo.request.BoardDeleteRequest;
import com.github.rlaehd62.vo.request.BoardListRequest;
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
		boardOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board No." + ID + "를 찾을 수 없습니다."));
		
		Board board = boardOptional.get();
		if(!util.isMine(board, request.getToken())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "오직 자신의 게시물만 수정 할 수 있습니다.");

		board.setContext(request.getContext());
		boardRepository.save(board);
	}
	
	@Override
	public BoardInfo get(long ID)
	{
		Optional<Board> boardOptional = boardRepository.findById(ID);
		boardOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "글이 존재하지 않습니다."));
		
		Board board = boardOptional.get();
		Account account = board.getAccount();
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
		boardOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "글이 존재하지 않습니다."));
		
		Board board = boardOptional.get();
		if(!util.isMine(board, request.getToken())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "오직 자신의 게시물만 수정 할 수 있습니다.");
		
		boardRepository.delete(board);
	}
}
