package com.github.rlaehd62.entity.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rlaehd62.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long>
{
	public List<Board> findAllByaccount_id(String ID, Pageable Pageable);
}
