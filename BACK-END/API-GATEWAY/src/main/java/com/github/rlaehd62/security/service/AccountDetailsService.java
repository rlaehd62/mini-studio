package com.github.rlaehd62.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.rlaehd62.security.AccountDetails;
import com.github.rlaehd62.vo.AccountInfo;

@Service
public class AccountDetailsService implements UserDetailsService
{
	private RestTemplate template;
	
	@Autowired
	public AccountDetailsService(RestTemplate template)
	{
		this.template = template;
	}
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		try
		{
			AccountInfo vo = template.getForObject("http://AUTH-SERVICE/accounts?id="+username, AccountInfo.class);	
			System.out.println(vo);
			return new AccountDetails(vo.getId(), vo.getPw(), vo.getUsername(), vo.getRoles());
		} catch (Exception e)
		{
			return null;
		}
	}

}
