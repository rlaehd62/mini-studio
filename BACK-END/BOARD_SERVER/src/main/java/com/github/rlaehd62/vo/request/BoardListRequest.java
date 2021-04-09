package com.github.rlaehd62.vo.request;

import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BoardListRequest
{
	private String accountID;
	private Pageable pageable;
}
