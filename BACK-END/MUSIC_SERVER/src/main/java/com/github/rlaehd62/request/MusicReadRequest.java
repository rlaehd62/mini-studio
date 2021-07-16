package com.github.rlaehd62.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MusicReadRequest
{
	private Long ID;
	private String token;
	private boolean isMine;
}
