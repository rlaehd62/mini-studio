package com.github.rlaehd62.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.github.rlaehd62.entity.auth.Account;
import com.github.rlaehd62.vo.account.AccountCreateRequest;
import com.github.rlaehd62.vo.AccountVO;
import com.github.rlaehd62.vo.RequestVO;
import com.github.rlaehd62.vo.TokenVO;
import com.github.rlaehd62.vo.request.account.AccountDeleteRequest;
import com.github.rlaehd62.vo.request.account.AccountFindRequest;
import com.github.rlaehd62.vo.request.account.AccountListRequest;
import com.github.rlaehd62.vo.request.account.AccountUpdateRequest;
import com.github.rlaehd62.vo.response.Info;

public interface AccountService
{
	@Transactional public TokenVO createAccount(AccountCreateRequest request, RequestVO requestVO);
	@Transactional public TokenVO updateAccount(AccountUpdateRequest request, RequestVO requestVO);
	@Transactional public void deleteAccount(AccountDeleteRequest request, RequestVO requestVO);
	public void findAccount(AccountFindRequest request);
	public Account getAccount(String token);
	public Account getAccountById(String id);
	public Map<String, Object> getAccountList(AccountListRequest request);

	public TokenVO verifyAccount(AccountVO vo, RequestVO requestVO);
}
