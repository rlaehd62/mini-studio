package com.github.rlaehd62.config.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.github.rlaehd62.config.event.reques.FileDeleteEvent;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;

@Component
public class FileDeleteListener
{
	@Autowired private RestTemplate restTemplate;
	
	@Subscribe
    public void listener(FileDeleteEvent event) 
    {
		Optional<String> optional = deleteFile(event);
		if(!optional.isPresent()) return;
		System.out.println(optional.get());
    }
	
	private Optional<String> deleteFile(FileDeleteEvent event)
	{
		try
		{
			Long ID = event.getFileID();
			if(ID <= 0) return Optional.absent();
			
			String msg = restTemplate.exchange("http://RESOURCE-SERVICE/mgmt/" + ID, HttpMethod.DELETE, null, String.class).getBody();
			return Optional.of(msg);
		} catch (Exception e)
		{
			return Optional.absent();
		}
	}
}
