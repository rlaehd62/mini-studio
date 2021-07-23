package com.github.rlaehd62.service.implemention;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rlaehd62.entity.auth.Account;
import com.github.rlaehd62.entity.auth.Block;
import com.github.rlaehd62.exception.AccountError;
import com.github.rlaehd62.exception.AccountException;
import com.github.rlaehd62.repository.BlockRepository;
import com.github.rlaehd62.service.AccountService;
import com.github.rlaehd62.service.BlockService;
import com.github.rlaehd62.vo.block.BlackListVO;
import com.github.rlaehd62.vo.request.block.BlockUserListRequest;
import com.github.rlaehd62.vo.request.block.BlockUserToggleEvent;

@Service("BlockService")
public class DefaultBlockService implements BlockService
{
	private BlockRepository blockRepository;
	private AccountService accountService;
	
	@Autowired
	public DefaultBlockService(BlockRepository blockRepository, DefaultAccountService accountService)
	{
		this.blockRepository = blockRepository;
		this.accountService = accountService;
	}

	@Override
	public void toggle(BlockUserToggleEvent event)
	{
		Optional<Block> optional = blockRepository.findByBlocked_id(event.getTargetID());
		
		if(!optional.isPresent())
		{
			Account account = accountService.getAccount(event.getToken());
			Account target = accountService.getAccountById(event.getTargetID());
			if(account.getId().equals(event.getTargetID()))  throw new AccountException(AccountError.DENY);	
			
			Block newBlock = new Block(account, target);
			blockRepository.saveAndFlush(newBlock);
			return;
		}
		
		blockRepository.delete(optional.get());
		return;
	}

	@Override
	public BlackListVO getBlackList(BlockUserListRequest event)
	{
		Account account = accountService.getAccount(event.getToken());
		List<Block> list = blockRepository.findByAccount_Id(account.getId());
		List<String> blackList = list.stream()
				.map(block -> block.getBlocked())
				.map(value -> value.getId())
				.collect(Collectors.toList());
		return new BlackListVO(blackList);
	}

}
