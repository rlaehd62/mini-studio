package com.github.rlaehd62.config.event.reques;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FileUploadEvent
{
	@NonNull private MultipartFile file;
	private boolean successful;
	private Long uploadedFileID;
}
