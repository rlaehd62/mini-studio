package com.github.rlaehd62.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MusicUploadRequest
{
	private String token;

	private String title;
	private String description;
	private String lyrics;
	
	private MultipartFile file;
}
