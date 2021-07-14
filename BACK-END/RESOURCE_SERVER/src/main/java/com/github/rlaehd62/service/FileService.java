package com.github.rlaehd62.service;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;

import com.github.rlaehd62.entity.file.File;
import com.github.rlaehd62.vo.resource.FileDeleteRequest;
import com.github.rlaehd62.vo.resource.FileRequest;
import com.github.rlaehd62.vo.resource.FileUploadRequest;

public interface FileService
{
	public <R> R uploadFile(FileUploadRequest request, Function<File, R> function) throws IllegalStateException, IOException;
	public void deleteFile(FileDeleteRequest request);
	public ResourceRegion stream(FileRequest request) throws IOException;
	
	default ResourceRegion calcuateByteRange(UrlResource resource, HttpHeaders headers) throws IOException
	{
        final long chunkSize = 500000L;
        long contentLength = resource.contentLength();
        Optional<HttpRange> optional = headers.getRange().stream().findFirst();
        
        if(optional.isPresent())
        {
        	HttpRange httpRange = optional.get();
            long start = httpRange.getRangeStart(contentLength);
            long end = httpRange.getRangeEnd(contentLength);
            long rangeLength = Long.min(chunkSize, end - start + 1);
            return new ResourceRegion(resource, start, rangeLength);
        } else
        {
            long rangeLength = Long.min(chunkSize, contentLength);
            return new ResourceRegion(resource, 0, rangeLength);
        }
	}
}
