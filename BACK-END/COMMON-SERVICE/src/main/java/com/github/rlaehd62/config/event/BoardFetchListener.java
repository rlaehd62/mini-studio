package com.github.rlaehd62.config.event;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.github.rlaehd62.config.event.reques.BoardFetchEvent;
import com.github.rlaehd62.entity.Board;
import com.github.rlaehd62.vo.TokenType;
import com.google.common.eventbus.Subscribe;

@Component
public class BoardFetchListener
{
	@Autowired private RestTemplate restTemplate;
	
	@Subscribe
    public void listener(BoardFetchEvent event) 
    {
		Optional<Board> optional = fetchBoard(event);
		if(optional.isPresent()) event.setResult(optional.get());
		event.setSuccessful(optional.isPresent());
    }
	
	private Optional<Board> fetchBoard(BoardFetchEvent event)
	{
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set(TokenType.ACCESS.getName(), event.getToken());
			HttpEntity<String> entity = new HttpEntity<String>("", headers);
			Board board = restTemplate.exchange("http://BOARD-SERVICE/boards/" + event.getID() + "?isMine="+event.isMine(), HttpMethod.GET, entity, Board.class).getBody();
			return Optional.of(board);
		} catch (Exception e)
		{
			e.printStackTrace();
			return Optional.empty();
		}
	}
}
