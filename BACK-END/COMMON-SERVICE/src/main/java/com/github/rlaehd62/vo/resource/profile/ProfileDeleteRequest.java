package com.github.rlaehd62.vo.resource.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ProfileDeleteRequest
{
    private String token;
}
