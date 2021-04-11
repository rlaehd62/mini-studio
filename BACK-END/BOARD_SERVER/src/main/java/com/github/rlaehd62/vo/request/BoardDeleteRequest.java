package com.github.rlaehd62.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BoardDeleteRequest
{
	private Long ID;
	private String token;
}
