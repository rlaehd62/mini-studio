package com.github.rlaehd62.repository;

import com.github.rlaehd62.entity.auth.Favor;
import com.github.rlaehd62.vo.MusicGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavorRepository extends JpaRepository<Favor, Long>
{
	List<Favor> findByAccount_Id(String Account_Id);
	Optional<Favor> findBygenre(MusicGenre genre);
}
