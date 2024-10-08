package com.github.rlaehd62.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.rlaehd62.entity.board.Board;
import com.github.rlaehd62.vo.TokenType;
import com.github.rlaehd62.vo.resource.board.BoardRequest;

@Service
public class BoardUtil
{
	@Autowired private RestTemplate restTemplate;
	
	public Optional<Board> findBoard(BoardRequest request)
	{
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set(TokenType.ACCESS.getName(), request.getToken());
			HttpEntity<String> entity = new HttpEntity<String>("", headers);
			
			Board board = restTemplate.exchange("http://BOARD-SERVICE/boards/" + request.getBoardID() + "?isMine=true", HttpMethod.GET, entity, Board.class).getBody();
			return Optional.of(board);
		} catch (Exception e)
		{
			e.printStackTrace();
			return Optional.empty();
		}
	}
}
