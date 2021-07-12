package com.github.rlaehd62.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MusicUpdateRequest
{
	private Long ID;
	private String token;

	private String title;
	private String description;
	private String lyrics;
}
