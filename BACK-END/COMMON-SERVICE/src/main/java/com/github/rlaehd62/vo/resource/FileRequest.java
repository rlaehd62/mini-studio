package com.github.rlaehd62.vo.resource;

import org.springframework.http.HttpHeaders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class FileRequest
{
	private Long ID;
	private HttpHeaders headers;
}
