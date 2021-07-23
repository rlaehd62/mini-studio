package com.github.rlaehd62.vo.request.account;

import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountListRequest
{
	@NonNull private String username;
	@NonNull private Pageable pageable;
}
