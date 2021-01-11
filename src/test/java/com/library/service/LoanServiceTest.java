package com.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
		//cenario
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
	
		//execucao
		when(loanRepository.save(savingLoan)).thenReturn(savedLoan);
		
		Loan loan = loanService.save(savingLoan);
		
		//verificacao
		assertThat(loan.getId()).isEqualTo(savedLoan.getId());
		assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
		assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
		assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
	}
	
}
