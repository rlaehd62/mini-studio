package com.github.rlaehd62.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BoardRequest
{
	@NonNull private Long ID;
	@NonNull private String token;
	private boolean isMine = false;
}
