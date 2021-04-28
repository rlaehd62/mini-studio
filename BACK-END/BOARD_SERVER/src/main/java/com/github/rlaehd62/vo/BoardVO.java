package com.github.rlaehd62.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardVO
{
	private Long ID;
	private String context;
	private String uploaderID;
	private String uploaderUsername;
	private LocalDateTime createdDate;
}
