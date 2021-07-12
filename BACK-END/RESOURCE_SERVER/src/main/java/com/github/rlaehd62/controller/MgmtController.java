package com.github.rlaehd62.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.rlaehd62.service.FileService;
import com.github.rlaehd62.service.Impl.DefaultFileService;
import com.github.rlaehd62.vo.resource.FileDeleteRequest;
import com.github.rlaehd62.vo.resource.FileUploadRequest;

@RestController
@RequestMapping("/mgmt")
public class MgmtController
{
	private FileService fileService;
	
	@Autowired
	public MgmtController(DefaultFileService fileService)
	{
		this.fileService = fileService;
	}
	
	@PostMapping("")
	public ResponseEntity<?> upload(@RequestParam MultipartFile file) throws IllegalStateException, IOException
	{
		FileUploadRequest request = new FileUploadRequest(file);
		return ResponseEntity.ok(fileService.uploadFile(request, (tempFile) -> { return tempFile.getID(); }));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long fileID)
	{
		FileDeleteRequest request = new FileDeleteRequest(fileID);
		fileService.deleteFile(request);
		return ResponseEntity.ok("File No." + fileID + " Deleted!");
	}
	
}
