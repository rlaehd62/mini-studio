package com.github.rlaehd62.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rlaehd62.entity.Music;

public interface MusicRepository extends JpaRepository<Music, Long>
{
	public List<Music> findAllByAccount_IdOrderByIDDesc(String Account_Id);
}
