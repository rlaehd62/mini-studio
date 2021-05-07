package com.github.rlaehd62.vo.account;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String id;
	private String mail;
	private String username;
}
