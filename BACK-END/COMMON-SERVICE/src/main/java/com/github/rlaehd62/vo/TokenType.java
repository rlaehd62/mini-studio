package com.github.rlaehd62.vo;

public enum TokenType
{
	ACCESS("ACCESS_TOKEN", 86400, false), 
	REFRESH("REFRESH_TOKEN", 604800, false);
	
	private String name;
	private int expiration;
	private boolean isCached;
	
	TokenType(String name, int expiration, boolean isCached)
	{
		this.setName(name);
		this.expiration = expiration;
		this.isCached = isCached;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}

	public int getExpiration()
	{
		return expiration;
	}

	public void setExpiration(int expiration)
	{
		this.expiration = expiration;
	}

	public boolean isCached()
	{
		return isCached;
	}

	public void setCached(boolean isCached)
	{
		this.isCached = isCached;
	}
}
