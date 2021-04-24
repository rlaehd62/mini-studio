package com.github.rlaehd62.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailVO
{
	private String from;
	private String to;
	private String title;
	private String message;
}
