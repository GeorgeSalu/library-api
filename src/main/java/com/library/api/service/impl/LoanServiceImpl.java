package com.library.api.service.impl;

import com.library.api.service.LoanService;
import com.library.exception.BusinessException;
import com.library.model.entity.Loan;
import com.library.model.repository.LoanRepository;

public class LoanServiceImpl implements LoanService{

	private LoanRepository loanRepository;

	public LoanServiceImpl(LoanRepository loanRepository) {
		this.loanRepository = loanRepository;
	}
	
	@Override
	public Loan save(Loan loan) {
		if(loanRepository.existsByBookAndNotReturned(loan.getBook())) {
			throw new BusinessException("Book already loaned");
		}
		return loanRepository.save(loan);
	}

}
