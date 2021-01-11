package com.library.api.service.impl;

import com.library.api.service.LoanService;
import com.library.model.entity.Loan;
import com.library.model.repository.LoanRepository;

public class LoanServiceImpl implements LoanService{

	private LoanRepository loanRepository;

	public LoanServiceImpl(LoanRepository loanRepository) {
		this.loanRepository = loanRepository;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Loan save(Loan loan) {
		// TODO Auto-generated method stub
		return loanRepository.save(loan);
	}

}
