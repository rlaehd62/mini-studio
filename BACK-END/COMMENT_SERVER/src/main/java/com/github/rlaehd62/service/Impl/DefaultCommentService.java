package com.github.rlaehd62.service.Impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.rlaehd62.config.event.reques.AccountCheckEvent;
import com.github.rlaehd62.config.event.reques.BlockCheckEvent;
import com.github.rlaehd62.config.event.reques.BoardFetchEvent;
import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.entity.Board;
import com.github.rlaehd62.entity.Comment;
import com.github.rlaehd62.exception.CommentError;
import com.github.rlaehd62.exception.CommentException;
import com.github.rlaehd62.repository.CommentRepository;
import com.github.rlaehd62.service.CommentService;
import com.github.rlaehd62.service.Util;
import com.github.rlaehd62.vo.Paging;
import com.github.rlaehd62.vo.comment.CommentVO;
import com.github.rlaehd62.vo.request.CommentDeleteRequest;
import com.github.rlaehd62.vo.request.CommentListRequest;
import com.github.rlaehd62.vo.request.CommentPatchRequest;
import com.github.rlaehd62.vo.request.CommentReadRequest;
import com.github.rlaehd62.vo.request.CommentUploadRequest;
import com.google.common.eventbus.EventBus;


@Service("DefaultCommentService")
public class DefaultCommentService implements CommentService
{
	private Util util;
	private EventBus eventBus;
	private CommentRepository repository;
	
	@Autowired
	public DefaultCommentService(Util util, EventBus eventBus, CommentRepository repository)
	{
		this.util = util;
		this.eventBus = eventBus;
		this.repository = repository;
	}
	
	@Override
	public Long upload(CommentUploadRequest request)
	{
		Long id = request.getID();
		String token = request.getToken();
		String context = request.getContext();
		
		Account account = util.findAccount(token);
		BoardFetchEvent event = new BoardFetchEvent(id, token);
		eventBus.post(event);
		
		if(event.isSuccessful())
		{
			Board board = event.getResult();
			Comment comment = Comment.builder()
					.board(board)
					.account(account)
					.context(context)
					.build();
			repository.saveAndFlush(comment);
			return comment.getID();
		}
		
		throw new CommentException(CommentError.PARENT_NOT_FOUND);
	}

	@Override
	public CommentVO update(CommentPatchRequest request)
	{
		Long id = request.getID();
		String token = request.getToken();
		String context = request.getContext();
		
		Optional<Comment> optional = repository.findById(id);
		if(!optional.isPresent()) throw new CommentException(CommentError.COMMENT_NOT_FOUND);
		
		Comment comment = optional.get();
		Account author = comment.getAccount();
		Board board = comment.getBoard();
		
		AccountCheckEvent checker = new AccountCheckEvent(token, board.getAccount());
		eventBus.post(checker);
		if(!checker.isSuccessful()) throw new CommentException(CommentError.COMMENT_ACCESS_DENIED);
		
		comment.setContext(context);
		repository.save(comment);

		return CommentVO.builder()
				.ID(id)
				.context(context)
				.accountID(author.getId())
				.accountUsername(author.getUsername())
				.date(comment.getCreatedDate())
				.build();
	}

	@Override
	public void delete(CommentDeleteRequest request)
	{
		Long id = request.getID();
		String token = request.getToken();
		
		Optional<Comment> optional = repository.findById(id);
		if(!optional.isPresent()) throw new CommentException(CommentError.COMMENT_NOT_FOUND);
		
		Comment comment = optional.get();
		Board board = comment.getBoard();
		
		AccountCheckEvent checker = new AccountCheckEvent(token, board.getAccount());
		eventBus.post(checker);
		if(!checker.isSuccessful()) throw new CommentException(CommentError.COMMENT_ACCESS_DENIED);
		
		repository.delete(comment);
	}

	@Override
	public CommentVO get(CommentReadRequest request)
	{
		Long id = request.getID();
		String token = request.getToken();
		boolean isMine = request.isMine();
		
		Optional<Comment> optional = repository.findById(id);
		if(!optional.isPresent()) throw new CommentException(CommentError.COMMENT_NOT_FOUND);
		
		Comment comment = optional.get();
		Account author = comment.getAccount();
		Board board = comment.getBoard();
		
		AccountCheckEvent checker = new AccountCheckEvent(token, board.getAccount());
		BlockCheckEvent block = new BlockCheckEvent(token, board.getAccount());
		eventBus.post(checker);
		
		if((isMine && !checker.isSuccessful()) || block.isSuccessful()) throw new CommentException(CommentError.COMMENT_ACCESS_DENIED);
		return CommentVO.builder()
				.ID(id)
				.context(comment.getContext())
				.accountID(author.getId())
				.accountUsername(author.getUsername())
				.date(comment.getCreatedDate())
				.build();
	}

	@Override
	public Map<String, Object> list(CommentListRequest request)
	{
		Long id = request.getID();
		String token = request.getToken();
		Pageable pageable = request.getPageable();
		
		BoardFetchEvent event = new BoardFetchEvent(id, token);
		eventBus.post(event);
		
		if(!event.isSuccessful()) throw new CommentException(CommentError.PARENT_NOT_FOUND);
		Page<Comment> page = repository.findAllByBoard_ID(id, pageable);
		List<Comment> list = page.getContent();
		
		Map<String, Object> map = new HashMap<>();
		Paging paging = new Paging(page);
		map.put("paging", paging);
		
		if(list.isEmpty()) 
		{
			map.put("list", Collections.emptyList());
			return map;
		}
		
		Board board = list.get(0).getBoard();
		BlockCheckEvent blockEvent = new BlockCheckEvent(token, board.getAccount());
		eventBus.post(blockEvent);
		
		if(blockEvent.isSuccessful()) throw new CommentException(CommentError.COMMENT_ACCESS_DENIED);
		
		map.put("list", list.stream()
				.map(comment -> CommentVO.builder()
						.ID(comment.getID())
						.context(comment.getContext())
						.accountID(comment.getAccount().getId())
						.accountUsername(comment.getAccount().getUsername())
						.date(comment.getCreatedDate())
						.build())
				.collect(Collectors.toList()));
		return map;
	}

}
