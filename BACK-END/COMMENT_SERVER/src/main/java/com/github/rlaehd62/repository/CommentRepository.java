package com.github.rlaehd62.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rlaehd62.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>
{
//	public List<Comment> findAllByBoard_IDAndContextContaining(Long Board_ID, String context, Pageable Pageable);
//	public List<Comment> findAllByBoard_ID(Long Board_ID, Pageable Pageable);
	public Page<Comment> findAllByBoard_ID(Long Board_ID, Pageable Pageable);
}
