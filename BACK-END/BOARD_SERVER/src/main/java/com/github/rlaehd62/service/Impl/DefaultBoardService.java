package com.github.rlaehd62.service.Impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.github.rlaehd62.messaging.DataFetcher;
import com.github.rlaehd62.vo.TokenType;
import com.github.rlaehd62.vo.account.AccountInfo;
import com.github.rlaehd62.vo.follow.FollowListVO;
import com.github.rlaehd62.vo.follow.FollowVO;
import com.github.rlaehd62.vo.request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.github.rlaehd62.config.event.reques.AccountCheckEvent;
import com.github.rlaehd62.config.event.reques.BlockCheckEvent;
import com.github.rlaehd62.entity.auth.Account;
import com.github.rlaehd62.entity.board.Board;
import com.github.rlaehd62.exception.BoardError;
import com.github.rlaehd62.exception.BoardException;
import com.github.rlaehd62.repository.BoardRepository;
import com.github.rlaehd62.service.BoardService;
import com.github.rlaehd62.service.Util;
import com.github.rlaehd62.vo.Paging;
import com.github.rlaehd62.vo.Public;
import com.github.rlaehd62.vo.board.BoardVO;
import com.google.common.eventbus.EventBus;

@Service
public class DefaultBoardService implements BoardService
{
	private Util util;
	private EventBus eventBus;
	private BoardRepository boardRepository;
	private DataFetcher fetcher;

	@Autowired
	public DefaultBoardService(Util util, EventBus eventBus, BoardRepository boardRepository, DataFetcher fetcher)
	{
		this.util = util;
		this.eventBus = eventBus;
		this.boardRepository = boardRepository;
		this.fetcher = fetcher;
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
			board.setContext(request.getContext());
			board.setIsPublic(request.getIsPublic());
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
	public Map<String, Object> list(BoardListRequest request)
	{		
		String ID = request.getAccountID();
		String KEYWORD = request.getKeyword();
		String token = request.getToken();
		Pageable pageable = request.getPageable();
		
		Map<String, Object> map = new HashMap<>();
		Page<Board> page = boardRepository.findAllByAccount_idAndContextContaining(ID, KEYWORD, pageable);
		List<Board> list = new ArrayList<>(page.getContent());
		
		Paging paging = new Paging(page);
		map.put("paging", paging);
		
		if(list.isEmpty()) 
		{
			map.put("list", Collections.emptyList());
			return map;
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
				.forEach(deleteList::add);
			list.removeAll(deleteList);
		}
		
		map.put("list", list.stream()
				.map(BoardVO::toBoardVO)
				.collect(Collectors.toList()));
		return map;
	}

	@Override
	public Map<String, Object> search(BoardFollowListRequest request)
	{
		String token = request.getToken();
		Optional<FollowListVO> optional = fetcher.fetch("AUTH-SERVICE/follows", HttpMethod.GET, fetcher.createHttpEntity(token), FollowListVO.class);

		String KEYWORD = request.getKeyword();
		Pageable pageable = request.getPageable();
		Map<String, Object> map = new HashMap<>();
		map.put("list", Collections.emptyList());

		return optional
				.map(FollowListVO::getFollows)
				.map(follows -> follows.stream().map(FollowVO::getTargetID).collect(Collectors.toList()))
				.map(followList ->
		{
			Page<Board> page = boardRepository.findAllByAccount_IdInAndContextContaining(followList, KEYWORD, pageable);
			List<Board> list = new ArrayList<>(page.getContent());

			Paging paging = new Paging(page);
			map.put("paging", paging);

			List<Board> deleteList = new ArrayList<>();
			list.stream()
					.filter(board -> board.getIsPublic().equals(Public.NO))
					.forEach(deleteList::add);
			list.removeAll(deleteList);

			map.put("list", list.stream()
					.map(BoardVO::toBoardVO)
					.collect(Collectors.toList()));
			return map;
		}).orElse(map);
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
