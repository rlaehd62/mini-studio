package com.github.rlaehd62.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rlaehd62.entity.auth.Account;

public interface AccountRepository extends JpaRepository<Account, String>
{
	Optional<Account> findAccountById(String id);
	public List<Account> findAllByIdContaining(String id);
	public Page<Account> findAllByUsernameContaining(String username, Pageable pageable);
	public List<Account> findAllByUsernameContaining(String username);
	boolean existsById(String id);
	boolean existsByIdAndPw(String id, String pw);
}
