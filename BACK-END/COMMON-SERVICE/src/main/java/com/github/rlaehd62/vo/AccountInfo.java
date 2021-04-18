package com.github.rlaehd62.vo;

import java.io.Serializable;
import java.util.List;

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
	private String pw;
	private String username;
	private List<String> roles;
}
