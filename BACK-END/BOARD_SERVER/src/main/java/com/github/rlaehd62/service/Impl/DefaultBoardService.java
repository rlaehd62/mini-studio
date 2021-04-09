package com.github.rlaehd62.service.Impl;

import java.util.Collections;
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
import com.github.rlaehd62.vo.AccountInfo;
import com.github.rlaehd62.vo.BoardInfo;
import com.github.rlaehd62.vo.request.BoardListRequest;
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
		Optional<AccountInfo> accountOptional = util.getAccountInfo(request.getToken());
		accountOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자가 존재하지 않습니다."));
		
		AccountInfo info = accountOptional.get();
		Account account = new Account(info.getId(), info.getPw(), info.getUsername(), Collections.emptyList());
		
		Board board = request.toBoard();
		board.setAccount(account);
		
		boardRepository.saveAndFlush(board);
		return board.getID();
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
		Pageable pageable = request.getPageable();
		return boardRepository.findAllByaccount_id(ID, pageable).stream()
				.map(value -> new BoardInfo(value.getID(), value.getContext(), value.getAccount().getId(), value.getAccount().getUsername()))
				.collect(Collectors.toList());
	}

}
