package com.github.rlaehd62.service.implemention;

import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.entity.Favor;
import com.github.rlaehd62.entity.Follow;
import com.github.rlaehd62.exception.AccountError;
import com.github.rlaehd62.exception.AccountException;
import com.github.rlaehd62.repository.FavorRepository;
import com.github.rlaehd62.repository.FollowRepository;
import com.github.rlaehd62.service.AccountService;
import com.github.rlaehd62.service.FavorService;
import com.github.rlaehd62.vo.favor.FavorListVO;
import com.github.rlaehd62.vo.favor.FavorVO;
import com.github.rlaehd62.vo.follow.FollowListVO;
import com.github.rlaehd62.vo.follow.FollowVO;
import com.github.rlaehd62.vo.request.favor.FavorListRequest;
import com.github.rlaehd62.vo.request.favor.FavorToggleEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("FavorService")
public class DefaultFavorService implements FavorService
{
	private FavorRepository favorRepository;
	private AccountService accountService;

	@Autowired
	public DefaultFavorService(FavorRepository followRepository, DefaultAccountService accountService)
	{
		this.favorRepository = followRepository;
		this.accountService = accountService;
	}
	
	@Override
	public void toggle(FavorToggleEvent event)
	{
		Account account = accountService.getAccount(event.getToken());
		Optional<Favor> optional = favorRepository.findBygenre(event.getFavor());

		optional
				.filter(favor -> Objects.nonNull(account))
				.ifPresentOrElse(favor -> favorRepository.delete(favor), () ->
				{
					Favor favor = Favor.builder()
							.account(account)
							.genre(event.getFavor())
							.build();
					favorRepository.saveAndFlush(favor);
				});
	}
	
	@Override
	public FavorListVO getFavorList(FavorListRequest event)
	{
		Account account = accountService.getAccount(event.getToken());
		List<Favor> list = favorRepository.findByAccount_Id(account.getId());
		List<FavorVO> voList = list.stream()
				.map(Favor -> FavorVO.builder()
						.ID(Favor.getID())
						.genre(Favor.getGenre())
						.build())
				.collect(Collectors.toList());
		return new FavorListVO(voList);
	}
}
