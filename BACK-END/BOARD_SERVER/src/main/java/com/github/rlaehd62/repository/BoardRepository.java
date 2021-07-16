package com.github.rlaehd62.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rlaehd62.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long>
{
//	public List<Board> findAllByAccount_idAndContextContaining(String ID, String context, Pageable Pageable);
//	public Page<Board> findAllByAccount_idAndContextContaining(String ID, String context, Pageable Pageable);
	public Page<Board> findAllByAccount_UsernameAndContextContaining(String Username, String context, Pageable Pageable);
}
