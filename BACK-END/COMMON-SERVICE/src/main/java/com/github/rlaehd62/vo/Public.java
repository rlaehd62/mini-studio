package com.github.rlaehd62.vo;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Public
{
	YES(1), NO(0), EMPTY(-1);
	
	Public(int value)
	{
		this.value = value;
	}
	
	private int value;
}
