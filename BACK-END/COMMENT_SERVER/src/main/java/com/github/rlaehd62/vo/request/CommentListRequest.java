package com.github.rlaehd62.vo.request;

import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentListRequest
{
	@NonNull private Long ID;
	@NonNull private String token;
	@NonNull private Pageable pageable;
}
