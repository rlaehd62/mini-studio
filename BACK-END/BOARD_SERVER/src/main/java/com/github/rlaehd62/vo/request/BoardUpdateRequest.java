package com.github.rlaehd62.vo.request;

import com.github.rlaehd62.vo.Public;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BoardUpdateRequest
{
	@NonNull private Long ID;
	@NonNull private String token;
	private String context;
	private Public isPublic;
}
