package com.github.rlaehd62.vo.request;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class FileUploadRequest
{
	{
		String TEMP_ID = UUID.randomUUID().toString();
		ID = TEMP_ID.replaceAll("-", "");
	}
	
	private String ID;
	@NonNull MultipartFile file;
}
