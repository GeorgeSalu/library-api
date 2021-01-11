package com.library.api.service.impl;

import com.library.api.service.LoanService;
import com.library.model.entity.Loan;
import com.library.model.repository.LoanRepository;

public class LoanServiceImpl implements LoanService{

	private LoanRepository loanRepository;

	public LoanServiceImpl(LoanRepository loanRepository) {
		this.loanRepository = loanRepository;
	}
	
	@Override
	public Loan save(Loan loan) {
		return loanRepository.save(loan);
	}

}
