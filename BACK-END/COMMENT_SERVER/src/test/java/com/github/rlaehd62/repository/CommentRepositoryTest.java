package com.github.rlaehd62.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.web.servlet.MockMvc;

import com.github.rlaehd62.entity.Comment;

@SpringBootTest
@AutoConfigureMockMvc
class CommentRepositoryTest
{
	@Autowired private  CommentRepository repo;
	private final Long ID = 79L;
	
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
}
