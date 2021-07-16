package com.github.rlaehd62.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.github.rlaehd62.exception.CommentError;
import com.github.rlaehd62.exception.CommentException;
import com.github.rlaehd62.service.Impl.DefaultCommentService;
import com.github.rlaehd62.vo.request.CommentUploadRequest;

@SpringBootTest
@AutoConfigureMockMvc
class CommentRepositoryTest
{
	@Autowired private  CommentRepository repo;
	@Autowired private DefaultCommentService service;
	private final Long ID = 79L;
	
	/*
	@Test
	@Order(0)
	void test1(@Autowired MockMvc mvc)
	{
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.DESC, "ID"));
		
		String keyword = "";
		List<Comment> comments = repo.findAllByBoard_IDAndContextContaining(ID, keyword, pageable);
		assertTrue(comments.size() >= 10);
		
		comments.stream()
			.map(comment -> comment.getContext())
			.forEach(comment -> System.out.println(comment));

		pageable = PageRequest.of(1, 10, Sort.by(Direction.DESC, "ID"));
		comments = repo.findAllByBoard_IDAndContextContaining(ID, keyword, pageable);
		assertTrue(comments.size() >= 10);
		
		comments.stream()
			.map(comment -> comment.getContext())
			.forEach(comment -> System.out.println(comment));
	}
	*/
	
	@Test()
	@Order(1)
	void test2(@Autowired MockMvc mvc) throws Exception
	{
		CommentUploadRequest request = new CommentUploadRequest
				(
						0L, 
						"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb290IiwiaWF0IjoxNjIxMDgyNzAxLCJleHAiOjE2MjExNjkxMDEsInVzZXJuYW1lIjoiS2ltRG9uZ0RvbmciLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXX0.XZUrbitHL9aMtVqmwjt6TZZ5kGAVfRYKlXNC9KQxxs0", 
						"테스트 내용"
				);
		
		CommentException exception = assertThrows(CommentException.class, () ->
		{
			service.upload(request);
		});
		
		assertTrue(exception.getError().equals(CommentError.COMMENT_NOT_FOUND));
	}
}
