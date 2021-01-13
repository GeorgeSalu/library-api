package com.library.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.library.model.entity.Loan;

@Service
public class SchedulerService {

	private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";
	private LoanService loanService;
	private EmailService emailService;
	
	@Value("${application.email.lateloans.message}")
	private String mensagem;
	
	public SchedulerService(LoanService loanService, EmailService emailService) {
		this.loanService = loanService;
		this.emailService = emailService;
	}
	
	@Scheduled(cron = CRON_LATE_LOANS)
	public void sendEmailToLateLoans() {
		List<Loan> allLateLoans = loanService.getAllLateLoans();
		List<String> mailsList = allLateLoans.stream()
					.map(loan -> loan.getCustomerEmail())
					.collect(Collectors.toList());
		
		
		emailService.sendEmails(mensagem, mailsList);
	}
}
