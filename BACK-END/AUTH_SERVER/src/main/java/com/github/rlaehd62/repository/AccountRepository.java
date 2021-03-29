package com.github.rlaehd62.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rlaehd62.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String>
{
	Optional<Account> findAccountById(String id);
	boolean existsById(String id);
	boolean existsByIdAndPw(String id, String pw);
}
