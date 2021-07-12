package com.github.rlaehd62.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MusicListRequest
{
	private String ID;
	private String token;
}
