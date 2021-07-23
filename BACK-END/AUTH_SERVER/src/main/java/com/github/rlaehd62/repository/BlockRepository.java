package com.github.rlaehd62.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rlaehd62.entity.auth.Block;

public interface BlockRepository extends JpaRepository<Block, Long>
{
	public List<Block> findByAccount_Id(String Account_Id);
	public Optional<Block> findByBlocked_id(String Blocked_id);
}
