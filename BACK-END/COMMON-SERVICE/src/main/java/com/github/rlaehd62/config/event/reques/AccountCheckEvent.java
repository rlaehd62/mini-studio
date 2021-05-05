package com.github.rlaehd62.config.event.reques;

import com.github.rlaehd62.entity.Account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountCheckEvent
{
	private String token;
	private boolean successful;
	private Account comparsion;
}
