package com.github.rlaehd62.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.entity.Board;
import com.github.rlaehd62.exception.BoardError;
import com.github.rlaehd62.exception.BoardException;
import com.github.rlaehd62.repository.BoardRepository;
import com.github.rlaehd62.service.BoardService;
import com.github.rlaehd62.service.Util;
import com.github.rlaehd62.vo.BoardVO;
import com.github.rlaehd62.vo.Public;
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
		String token = request.getToken();
		
		Optional<Board> boardOptional = boardRepository.findById(ID);
		boardOptional.orElseThrow(() -> new BoardException(BoardError.BOARD_NOT_FOUND));
		
		Board board = boardOptional.get();
		Account account = board.getAccount();
		
		Function<Account, Boolean> func = new BoardFunction(account);
		if(!util.isMine(func, token)) throw new BoardException(BoardError.BOARD_NOT_MINE);

		if(!request.getContext().isEmpty()) board.setContext(request.getContext());
		if(!request.getIsPublic().equals(Public.EMPTY)) board.setIsPublic(request.getIsPublic());
		boardRepository.save(board);
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
		Function<Account, Boolean> func = new BoardFunction(account);
		if((isMine || isPublic.equals(Public.NO)) && !util.isMine(func, TOKEN)) throw new BoardException(BoardError.BOARD_NOT_MINE);
		
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
		
		Account account = new Account(ID, null, null);
		Function<Account, Boolean> func = new BoardFunction(account);
		
		Stream<Board> stream = boardRepository.findAllByAccount_idAndContextContaining(ID, KEYWORD, pageable).stream();
		boolean result = util.isMine(func, token);
		
		if(!result) 
		{
			return stream.filter(value -> value.getIsPublic().equals(Public.YES))
					.map(value -> BoardVO.builder()
					.ID(value.getID())
					.context(value.getContext())
					.uploaderID(value.getAccount().getId())
					.uploaderUsername(value.getAccount().getUsername())
					.createdDate(value.getCreatedDate())
					.build())
			.collect(Collectors.toList());
		}
		
		return stream
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
		
		Function<Account, Boolean> func = new BoardFunction(account);
		if(!util.isMine(func, token)) throw new BoardException(BoardError.BOARD_NOT_MINE);
		
		boardRepository.delete(board);
	}
}
