package com.github.rlaehd62.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardDeleteRequest
{
	private Long boardFileID;
	private String token;
}
