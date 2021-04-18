package com.github.rlaehd62.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rlaehd62.entity.BoardFile;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long>
{
	public List<BoardFile> findAllByBoard_ID(Long Board_ID);
}
