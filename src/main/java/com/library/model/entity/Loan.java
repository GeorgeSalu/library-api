package com.library.model.entity;

import java.time.LocalDate;

import com.library.api.dto.LoanDTO;

public class Loan {
	private Long id;
	private String customer;
	private Book book;
	private LocalDate loanDate;
	private Boolean returned;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public LocalDate getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(LocalDate loanDate) {
		this.loanDate = loanDate;
	}

	public Boolean getReturned() {
		return returned;
	}

	public void setReturned(Boolean returned) {
		this.returned = returned;
	}

	public static Loan criaLoan(LoanDTO dto, Book book) {

		Loan loan = new Loan();
		loan.setBook(book);
		loan.setCustomer(dto.getCustomer());
		loan.setLoanDate(LocalDate.now());
		return loan;
	}

}
