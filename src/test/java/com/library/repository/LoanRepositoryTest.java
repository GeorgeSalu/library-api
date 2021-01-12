package com.library.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.library.model.entity.Book;
import com.library.model.entity.Loan;
import com.library.model.repository.LoanRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

	@Autowired
	private LoanRepository repository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	@DisplayName("Deve verificar se existe emprestimo não devolvido para o livro.")
	public void existsByBookAndNotReturnedTest() {
		// cenario
		Book book = createValidBook("123");
		entityManager.persist(book);
		
		Loan loan = new Loan();
		loan.setBook(book);
		loan.setCustomer("Fulano");
		loan.setLoanDate(LocalDate.now());
		
		
		entityManager.persist(loan);
		
		//execucao
		boolean exists = repository.existsByBookAndNotReturned(book);
		
		assertThat(exists).isTrue();
	}
	
	@Test
	@DisplayName("Deve buscar emprestimos pelo isbn do livro ou customer")
	public void findByBookIsbnOrCustomerTest() {
		
		// cenario
		Book book = createValidBook("123");
		entityManager.persist(book);
		
		Loan loan = new Loan();
		loan.setBook(book);
		loan.setCustomer("Fulano");
		loan.setLoanDate(LocalDate.now());
		
		
		entityManager.persist(loan);
		
		Page<Loan> result = repository.findByBookIsbnOrCustomer("123", "Fulano", PageRequest.of(0, 10));
		
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getPageable().getPageSize()).isEqualTo(10);
		
	}
	
	private Book createValidBook(String isbn) {
		Book book = new Book();
		book.setTitle("As aventuras");
		book.setAuthor("Fulano");
		book.setIsbn(isbn);
		return book;
	}
	
}
