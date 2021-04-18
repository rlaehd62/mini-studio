package com.github.rlaehd62;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.rlaehd62.service.Impl.DefaultBoardFileService;
import com.github.rlaehd62.vo.BoardListVO;
import com.github.rlaehd62.vo.request.BoardRequest;

@SpringBootTest
@AutoConfigureMockMvc
class ResourceServerApplicationTests 
{
	@Autowired private DefaultBoardFileService service;
	private static BoardRequest request;
	private static BoardRequest badRequest;
	
	@BeforeAll
	static void ready()
	{
		request = new BoardRequest(26L);
		badRequest = new BoardRequest(99L);
	}

	@Test
	void test() 
	{
		BoardListVO vo = service.get(request);
		List<Long> idList = vo.getList();
		assertFalse(idList.isEmpty());
		System.out.println(idList);
	}
	
	@Test
	void testError()
	{
		BoardListVO vo = service.get(badRequest);
		List<Long> idList = vo.getList();
		assertTrue(idList.isEmpty());
	}
}
