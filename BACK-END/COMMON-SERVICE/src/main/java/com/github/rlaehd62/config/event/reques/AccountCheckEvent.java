package com.github.rlaehd62.config.event.reques;

import com.github.rlaehd62.entity.auth.Account;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AccountCheckEvent
{
	@NonNull private String token;
	private boolean successful;
	private boolean privileged;
	private Account privilegedResult;
	@NonNull private Account comparsion;
}
