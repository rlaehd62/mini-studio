package com.github.rlaehd62.config.event;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.github.rlaehd62.config.event.reques.FileUploadEvent;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;

@Component
public class FileUploadListener
{
	@Autowired private RestTemplate restTemplate;
	
	@Subscribe
    public void listener(FileUploadEvent event) 
    {
		Optional<Long> optional = uploadFile(event);
		if(!optional.isPresent()) return;
		
		event.setSuccessful(true);
		event.setUploadedFileID(optional.get());
    }
	
	private Optional<Long> uploadFile(FileUploadEvent event)
	{
		try
		{
			MultipartFile file = event.getFile();
			if(Objects.isNull(file)) return Optional.absent();
			
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
			parameters.add("file", file.getResource());
			          
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			
			HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(parameters, headers);
			String ID = restTemplate.postForEntity("http://RESOURCE-SERVICE/mgmt", formEntity, String.class).getBody();
			
			return Optional.of(Long.parseLong(ID));
		} catch (Exception e)
		{
			e.printStackTrace();
			return Optional.absent();
		}
	}
}
