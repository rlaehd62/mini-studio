package com.github.rlaehd62.service;

import java.util.List;

import javax.transaction.Transactional;

import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.vo.AccountCreateRequest;
import com.github.rlaehd62.vo.AccountVO;
import com.github.rlaehd62.vo.RequestVO;
import com.github.rlaehd62.vo.TokenVO;
import com.github.rlaehd62.vo.request.AccountDeleteRequest;
import com.github.rlaehd62.vo.request.AccountFindRequest;
import com.github.rlaehd62.vo.request.AccountListRequest;
import com.github.rlaehd62.vo.request.AccountRequest;
import com.github.rlaehd62.vo.request.AccountUpdateRequest;
import com.github.rlaehd62.vo.response.Info;

public interface AccountService
{
	@Transactional public TokenVO createAccount(AccountCreateRequest request, RequestVO requestVO);
	@Transactional public TokenVO updateAccount(AccountUpdateRequest request, RequestVO requestVO);
	@Transactional public void deleteAccount(AccountDeleteRequest request, RequestVO requestVO);
	public void findAccount(AccountFindRequest request);
	
	public AccountVO getAccountVO(AccountRequest request);
	public Account getAccount(String token);
	public List<Info> getAccountList(AccountListRequest request);	

	public TokenVO verifyAccount(AccountVO vo, RequestVO requestVO);
}
