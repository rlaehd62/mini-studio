package com.github.rlaehd62.service.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.rlaehd62.entity.File;
import com.github.rlaehd62.entity.repository.FileRepository;
import com.github.rlaehd62.exception.FileError;
import com.github.rlaehd62.exception.FileException;
import com.github.rlaehd62.service.FileService;
import com.github.rlaehd62.vo.resource.FileDeleteRequest;
import com.github.rlaehd62.vo.resource.FileRequest;
import com.github.rlaehd62.vo.resource.FileUploadRequest;

@Service
public class DefaultFileService implements FileService
{
	@Value("${fileUpload.root}") private String root;
	private FileRepository fileRepository;
	
	@Autowired
	public DefaultFileService(FileRepository fileRepository)
	{
		this.fileRepository = fileRepository;
	}

	@Override
	public <R> R uploadFile(FileUploadRequest request, Function<File, R> function)
			throws IllegalStateException, IOException
	{
		MultipartFile mp = request.getFile();
		Path path = Paths.get(root, request.getID(), mp.getOriginalFilename());
		
		path.toFile().mkdirs();
		Files.copy(mp.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		
		File file = new File(mp.getOriginalFilename(), path.toString());
		fileRepository.saveAndFlush(file);
		
		return function.apply(file);
	}
	
	@Override
	public void deleteFile(FileDeleteRequest request)
	{
		Long ID = request.getID();
		Optional<File> optional = fileRepository.findById(ID);
		optional.orElseThrow(() -> new FileException(FileError.FILE_NOT_FOUND));
		
		File file = optional.get();
		fileRepository.delete(file);
	}

	@Override
	public ResourceRegion stream(FileRequest request) throws IOException
	{
		Long ID = request.getID();
		Optional<File> optional = fileRepository.findById(ID);
		optional.orElseThrow(() -> new FileException(FileError.FILE_NOT_FOUND));
		
		File file = optional.get();
		UrlResource resource = new UrlResource("file", file.getPath());
		HttpHeaders headers = request.getHeaders();
		
		return calcuateByteRange(resource, headers);
	}


}
