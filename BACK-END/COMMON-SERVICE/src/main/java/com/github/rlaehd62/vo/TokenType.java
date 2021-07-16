package com.github.rlaehd62.vo;

public enum TokenType
{
	ACCESS("ACCESS_TOKEN");
	
	private String name;
	
	TokenType(String name)
	{
		this.setName(name);
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
}
