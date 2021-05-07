package com.github.rlaehd62.service.implemention;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.entity.Follow;
import com.github.rlaehd62.exception.AccountError;
import com.github.rlaehd62.exception.AccountException;
import com.github.rlaehd62.repository.FollowRepository;
import com.github.rlaehd62.service.AccountService;
import com.github.rlaehd62.service.FollowService;
import com.github.rlaehd62.vo.follow.FollowListVO;
import com.github.rlaehd62.vo.follow.FollowVO;
import com.github.rlaehd62.vo.request.follow.FollowListRequest;
import com.github.rlaehd62.vo.request.follow.FollowToggleEvent;

@Service("FollowService")
public class DefaultFollowService implements FollowService
{
	private FollowRepository followRepository;
	private AccountService accountService;
	
	@Autowired
	public DefaultFollowService(FollowRepository followRepository, DefaultAccountService accountService)
	{
		this.followRepository = followRepository;
		this.accountService = accountService;
	}
	
	@Override
	public void toggle(FollowToggleEvent event)
	{
		Account account = accountService.getAccount(event.getToken());
		Account targetAccount = accountService.getAccountById(event.getTargetID());
		
		String acID = account.getId();
		String tgID = targetAccount.getId();
		if(acID.equals(tgID)) throw new AccountException(AccountError.DENY);	
		
		Optional<Follow> optional = followRepository.findByFollower_IdAndFollowee_Id(acID, tgID);
		if(!optional.isPresent())
		{
			Follow follow = new Follow(account, targetAccount);
			followRepository.save(follow);
			return;
		}
		
		followRepository.delete(optional.get());
	}
	
	@Override
	public FollowListVO getFollowList(FollowListRequest event)
	{
		Account account = accountService.getAccount(event.getToken());
		List<Follow> list = followRepository.findByFollower_Id(account.getId());
		List<FollowVO> voList = list.stream()
				.map(follow -> follow.getFollowee())
				.map(followee -> FollowVO.builder()
						.targetID(followee.getId())
						.targetUsername(followee.getUsername())
						.build())
				.collect(Collectors.toList());
		return new FollowListVO(voList);
	}
}
