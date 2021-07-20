package com.github.rlaehd62.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BoardFollowListRequest
{
    private String token;
    @NonNull private String keyword;
    @NonNull private Pageable pageable;
}
