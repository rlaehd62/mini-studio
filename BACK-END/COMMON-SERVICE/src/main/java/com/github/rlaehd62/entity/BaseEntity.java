package com.github.rlaehd62.entity;

import java.time.LocalDateTime;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity
{
	@CreatedDate
	@JsonFormat(timezone = "Asia/Seoul")
	@DateTimeFormat(pattern = "yyyyMMddHHmmss")
	private LocalDateTime createdDate;
	
	@PostConstruct
	void init() 
	{
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
}
