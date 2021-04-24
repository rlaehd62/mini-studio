package com.github.rlaehd62.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.github.rlaehd62.vo.MailVO;

@Service
public class MailService
{

	@Autowired private JavaMailSender mailSender;
	
    public void mailSend(MailVO vo) 
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(vo.getTo());
        message.setFrom(vo.getFrom());
        message.setSubject(vo.getTitle());
        message.setText(vo.getMessage());

        mailSender.send(message);
    }
}
