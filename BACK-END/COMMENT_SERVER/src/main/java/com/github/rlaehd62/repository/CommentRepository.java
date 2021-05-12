package com.github.rlaehd62.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rlaehd62.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>
{
	public List<Comment> findAllByBoard_IDAndContextContaining(Long Board_ID, String context, Pageable Pageable);
}
