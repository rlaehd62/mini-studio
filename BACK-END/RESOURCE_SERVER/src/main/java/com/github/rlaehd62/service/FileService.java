package com.github.rlaehd62.service;

import java.io.IOException;
import java.util.function.Function;

import com.github.rlaehd62.entity.File;
import com.github.rlaehd62.vo.request.FileUploadRequest;

public interface FileService
{
	public <R> R uploadFile(FileUploadRequest request, Function<File, R> function) throws IllegalStateException, IOException;
}
