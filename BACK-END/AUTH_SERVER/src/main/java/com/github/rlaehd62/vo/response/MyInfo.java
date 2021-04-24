package com.github.rlaehd62.vo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyInfo
{
	private String id;
	private String username;
	private String email;
}
