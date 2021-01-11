package com.library.service;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.library.api.service.LoanService;
import com.library.api.service.impl.LoanServiceImpl;
import com.library.model.entity.Book;
import com.library.model.entity.Loan;
import com.library.model.repository.LoanRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

	@MockBean
	private LoanRepository loanRepository;
	
	private LoanService loanService;

	@BeforeEach
	public void setUp() {
		this.loanService = new LoanServiceImpl(loanRepository);
	}
	
	@Test
	@DisplayName("Deve salvar um emprestimo")
	public void saveLoanTest() {
		
		Book book = new Book();
		book.setId(1l);
		
		Loan savingLoan = new Loan();
		savingLoan.setBook(book);
		savingLoan.setCustomer("Fulano");
		savingLoan.setLoanDate(LocalDate.now());
		
		Loan savedLoan = new Loan();
		savedLoan.setId(1l);
		savedLoan.setLoanDate(LocalDate.now());
		savedLoan.setBook(book);
		savedLoan.setCustomer("Fulano");
		
		Mockito.when(loanRepository.save(savingLoan)).thenReturn(savedLoan);
		
		Loan loan = loanService.save(savingLoan);
		
		Assertions.assertThat(loan.getId()).isEqualTo(savedLoan.getId());
		Assertions.assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
	}
	
}
