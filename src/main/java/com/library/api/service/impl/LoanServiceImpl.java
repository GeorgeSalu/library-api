package com.library.api.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.library.api.dto.LoanFilterDTO;
import com.library.api.service.LoanService;
import com.library.exception.BusinessException;
import com.library.model.entity.Book;
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

	@Override
	public Optional<Loan> getById(Long id) {
		return loanRepository.findById(id);
	}

	@Override
	public Loan update(Loan loan) {
		return loanRepository.save(loan);
	}

	@Override
	public Page<Loan> find(LoanFilterDTO filterDTO, Pageable pageable) {
		return loanRepository.findByBookIsbnOrCustomer(filterDTO.getIsbn(), filterDTO.getCustomer(), pageable);
	}

	@Override
	public Page<Loan> getLoansByBook(Book book, Pageable pageable) {
		return loanRepository.findByBook(book, pageable);
	}

}
