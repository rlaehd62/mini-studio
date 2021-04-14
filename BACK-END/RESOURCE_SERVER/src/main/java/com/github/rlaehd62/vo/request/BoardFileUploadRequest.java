package com.github.rlaehd62.vo.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardFileUploadRequest
{
	private Long boardID;
	private String token;
	private List<MultipartFile> files;
}
