package com.github.rlaehd62.vo.request;

import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BoardListRequest
{
	@NonNull String accountID;
	@NonNull private Pageable pageable;
	private String keyword;
	private String token;
	
}
