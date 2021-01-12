package com.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
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
import com.library.exception.BusinessException;
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
		when(loanRepository.existsByBookAndNotReturned(book)).thenReturn(false);
		when(loanRepository.save(savingLoan)).thenReturn(savedLoan);
		
		Loan loan = loanService.save(savingLoan);
		
		//verificacao
		assertThat(loan.getId()).isEqualTo(savedLoan.getId());
		assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
		assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
		assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
	}
	
	@Test
	@DisplayName("Deve lancar erro de negocio ao salvar um emprestimo com livro ja emprestado")
	public void loanedBookSaveTest() {
		//cenario
		Book book = new Book();
		book.setId(1l);
		
		Loan savingLoan = new Loan();
		savingLoan.setBook(book);
		savingLoan.setCustomer("Fulano");
		savingLoan.setLoanDate(LocalDate.now());
		
		when(loanRepository.existsByBookAndNotReturned(book)).thenReturn(true);
		Throwable exception = Assertions.catchThrowable(() -> loanService.save(savingLoan));
		
		assertThat(exception).isInstanceOf(BusinessException.class).hasMessage("Book already loaned");
		
	}
	
	@Test
	@DisplayName("Deve obter as informações de um emprestimo pelo ID")
	public void getLoanDetaislTest() {
		//cenario
		Loan loan = createLoan();
		loan.setId(1l);
		
		Mockito.when(loanRepository.findById(1l)).thenReturn(Optional.of(loan));
		
		//execucao
		Optional<Loan> result = loanService.getById(1l);
		
		//verificacao
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getId()).isEqualTo(1l);
		assertThat(result.get().getCustomer()).isEqualTo(loan.getCustomer());
		
		Mockito.verify(loanRepository).findById(1l);
	}
	
	@Test
	@DisplayName("Deve atualizar um emprestimo.")
	public void updateLoanTest() {
		Loan loan = createLoan();
		loan.setId(1l);
		loan.setReturned(true);
		
		when(loanRepository.save(loan)).thenReturn(loan);
		
		Loan updateLoan = loanService.update(loan);
		
		assertThat(updateLoan.getReturned()).isTrue();
		
		Mockito.verify(loanRepository).save(loan);
	}
	
	public Loan createLoan() {
		Book book = new Book();
		book.setId(1l);
		
		Loan savingLoan = new Loan();
		savingLoan.setBook(book);
		savingLoan.setCustomer("Fulano");
		savingLoan.setLoanDate(LocalDate.now());
		return savingLoan;
	}
	
}
