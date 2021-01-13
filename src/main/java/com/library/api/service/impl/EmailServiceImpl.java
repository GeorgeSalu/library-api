package com.library.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.library.api.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	@Value("${application.email.default-remetent}")
	private String remetent;
	
	private JavaMailSender javaMailSender;

	public EmailServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	@Override
	public void sendEmails(String mensagem, List<String> mailsList) {
		
		String[] mails = mailsList.toArray(new String[mailsList.size()]);
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(remetent);
		mailMessage.setSubject("Livro com emprestimo atrasado");
		mailMessage.setText(mensagem);
		mailMessage.setTo(mails);

		javaMailSender.send(mailMessage);
	}

}
