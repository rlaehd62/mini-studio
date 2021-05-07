package com.github.rlaehd62.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.rlaehd62.config.event.reques.AccountCheckEvent;
import com.github.rlaehd62.config.event.reques.BlockCheckEvent;
import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.entity.Board;
import com.github.rlaehd62.exception.BoardError;
import com.github.rlaehd62.exception.BoardException;
import com.github.rlaehd62.repository.BoardRepository;
import com.github.rlaehd62.service.BoardService;
import com.github.rlaehd62.service.Util;
import com.github.rlaehd62.vo.Public;
import com.github.rlaehd62.vo.board.BoardVO;
import com.github.rlaehd62.vo.request.BoardDeleteRequest;
import com.github.rlaehd62.vo.request.BoardListRequest;
import com.github.rlaehd62.vo.request.BoardRequest;
import com.github.rlaehd62.vo.request.BoardUpdateRequest;
import com.github.rlaehd62.vo.request.BoardUploadRequest;
import com.google.common.eventbus.EventBus;

@Service
public class DefaultBoardService implements BoardService
{
	private Util util;
	private EventBus eventBus;
	private BoardRepository boardRepository;
	
	@Autowired
	public DefaultBoardService(Util util, EventBus eventBus, BoardRepository boardRepository)
	{
		this.util = util;
		this.eventBus = eventBus;
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
	public BoardVO update(BoardUpdateRequest request)
	{
		Long ID = request.getID();
		String token = request.getToken();
		
		Optional<Board> boardOptional = boardRepository.findById(ID);
		boardOptional.orElseThrow(() -> new BoardException(BoardError.BOARD_NOT_FOUND));
		
		Board board = boardOptional.get();
		Account account = board.getAccount();
		
		AccountCheckEvent event = new AccountCheckEvent(token, account);
		eventBus.post(event);
		
		if(!event.isSuccessful()) throw new BoardException(BoardError.BOARD_NOT_MINE);
		else
		{
			if(!request.getContext().equals("")) board.setContext(request.getContext());
			if(!request.getIsPublic().equals(Public.EMPTY)) board.setIsPublic(request.getIsPublic());
			boardRepository.saveAndFlush(board);			
		}

		
		BoardVO vo = BoardVO.builder()
				.ID(board.getID())
				.context(board.getContext())
				.createdDate(board.getCreatedDate())
				.uploaderID(board.getAccount().getId())
				.uploaderUsername(board.getAccount().getUsername())
				.build();
		
		return vo;
	}
	
	@Override
	public BoardVO get(BoardRequest request)
	{
		Long ID = request.getID();
		String TOKEN = request.getToken();
		boolean isMine = request.isMine();
		
		Optional<Board> boardOptional = boardRepository.findById(ID);
		boardOptional.orElseThrow(() -> new BoardException(BoardError.BOARD_NOT_FOUND));
		
		Board board = boardOptional.get();
		Account account = board.getAccount();
		
		Public isPublic = board.getIsPublic();
		
		AccountCheckEvent event = new AccountCheckEvent(TOKEN, account);
		BlockCheckEvent blockEvent = new BlockCheckEvent(TOKEN, account);
		
		eventBus.post(event);		
		eventBus.post(blockEvent);
		
		if((isMine || isPublic.equals(Public.NO)) && !event.isSuccessful()) throw new BoardException(BoardError.BOARD_NOT_MINE);
		else if(blockEvent.isSuccessful()) throw new BoardException(BoardError.BOARD_NOT_MINE);

		
		return BoardVO.builder()
				.ID(board.getID())
				.context(board.getContext())
				.uploaderID(account.getId())
				.uploaderUsername(account.getUsername())
				.createdDate(board.getCreatedDate())
				.build();
	}

	@Override
	public List<BoardVO> list(BoardListRequest request)
	{		
		String ID = request.getAccountID();
		String KEYWORD = request.getKeyword();
		String token = request.getToken();
		Pageable pageable = request.getPageable();
		
		List<Board> list = boardRepository.findAllByAccount_idAndContextContaining(ID, KEYWORD, pageable);
		if(list.isEmpty()) 
		{
			return list.stream()
					.map(value -> BoardVO.builder()
							.ID(value.getID())
							.context(value.getContext())
							.uploaderID(value.getAccount().getId())
							.uploaderUsername(value.getAccount().getUsername())
							.createdDate(value.getCreatedDate())
							.build())
					.collect(Collectors.toList());
		}
		
		Account account = list.get(0).getAccount();
		AccountCheckEvent event = new AccountCheckEvent(token, account);
		BlockCheckEvent blockEvent = new BlockCheckEvent(token, account);
		eventBus.post(event);
		eventBus.post(blockEvent);
		
		if(blockEvent.isSuccessful()) throw new BoardException(BoardError.BOARD_NOT_MINE);
		else if(!event.isSuccessful()) 
		{
			List<Board> deleteList = new ArrayList<>();
			list.stream()
				.filter(board -> board.getIsPublic().equals(Public.NO))
				.forEach(board -> deleteList.add(board));
			list.removeAll(deleteList);
		}
		
		return list.stream()
				.map(value -> BoardVO.builder()
				.ID(value.getID())
				.context(value.getContext())
				.uploaderID(value.getAccount().getId())
				.uploaderUsername(value.getAccount().getUsername())
				.createdDate(value.getCreatedDate())
				.build())
		.collect(Collectors.toList());
	}

	@Override
	public void delete(BoardDeleteRequest request)
	{
		Long ID = request.getID();
		String token = request.getToken();
		
		Optional<Board> boardOptional = boardRepository.findById(ID);
		boardOptional.orElseThrow(() -> new BoardException(BoardError.BOARD_NOT_FOUND));
		
		Board board = boardOptional.get();
		Account account = board.getAccount();
		
		AccountCheckEvent event = new AccountCheckEvent(token, account);
		eventBus.post(event);
		if(!event.isSuccessful()) throw new BoardException(BoardError.BOARD_NOT_MINE);
		
		boardRepository.delete(board);
	}
}
