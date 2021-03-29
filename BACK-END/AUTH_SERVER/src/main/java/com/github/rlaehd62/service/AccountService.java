package com.github.rlaehd62.service;

import javax.transaction.Transactional;

import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.vo.AccountVO;
import com.github.rlaehd62.vo.RequestVO;
import com.github.rlaehd62.vo.TokenVO;

public interface AccountService
{
	@Transactional public TokenVO createAccount(AccountVO vo, RequestVO requestVO);
	public AccountVO getAccountVO(String id);
	public Account getAccount(String token);
	@Transactional public TokenVO updateAccount(String token, AccountVO vo, RequestVO requestVO);
	@Transactional public void deleteAccount(String token, RequestVO requestVO);
	public TokenVO verifyAccount(AccountVO vo, RequestVO requestVO);
}
