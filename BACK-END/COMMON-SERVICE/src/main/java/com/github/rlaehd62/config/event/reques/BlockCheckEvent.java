package com.github.rlaehd62.config.event.reques;

import com.github.rlaehd62.entity.Account;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BlockCheckEvent
{
	@NonNull private String token;
	private boolean successful;
	@NonNull private Account comparsion;
}
