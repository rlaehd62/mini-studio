package com.github.rlaehd62.service.Impl;

import java.util.function.Function;

import com.github.rlaehd62.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Deprecated
public class BoardFunction implements Function<Account, Boolean>
{
	private Account account;
	public Boolean apply(Account t)
	{
		String uploader = account.getId();
		String tempID = t.getId();
		return tempID.equals(uploader);
	}
}
