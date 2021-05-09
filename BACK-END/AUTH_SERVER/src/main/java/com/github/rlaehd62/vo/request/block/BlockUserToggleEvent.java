package com.github.rlaehd62.vo.request.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockUserToggleEvent
{
	private String token;
	private String targetID;
}
