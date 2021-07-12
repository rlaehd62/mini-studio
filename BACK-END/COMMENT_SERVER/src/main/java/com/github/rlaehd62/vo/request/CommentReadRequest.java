package com.github.rlaehd62.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentReadRequest
{
	@NonNull private Long ID;
	@NonNull private String token;
	@NonNull private boolean isMine;
}
