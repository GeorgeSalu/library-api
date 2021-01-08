package com.library.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.library.model.entity.Book;
import com.library.model.repository.BookRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

	@Autowired
	TestEntityManager entityManager;
	
	@Autowired
	BookRepository repository;
	
	@Test
	@DisplayName("Deve retornar verdadeiro quando existir um livro na base com isbn informado.")
	public void returnTrueWhenIsbnExists() {
		//cenario
		String isbn = "123";
		Book book = createValidBook(isbn);
		entityManager.persist(book);
		
		//execucao
		boolean exists = repository.existsByIsbn(isbn);
		
		//verificacao
		assertThat(exists).isTrue();
	}
	
	@Test
	@DisplayName("Deve retornar falso quando não existir um livro na base com isbn informado.")
	public void returnFalseWhenIsbnDoesntExists() {
		//cenario
		String isbn = "123";
		
		//execucao
		boolean exists = repository.existsByIsbn(isbn);
		
		//verificacao
		assertThat(exists).isFalse();
	}
	
	@Test
	@DisplayName("Deve criar um livro por id.")
	public void findByIdTest() {
		//cenario
		Book book = createValidBook("123");
		entityManager.persist(book);
		
		//execucao
		Optional<Book> foundBook = repository.findById(book.getId());
		
		//verificacao
		assertThat(foundBook.isPresent()).isTrue();
	}
	
	@Test
	@DisplayName("Deve salvar um livro")
	public void saveBookTest() {
		Book book = createValidBook("123");
		
		Book savedBook = repository.save(book);
		
		//verificacao
		assertThat(savedBook.getId()).isNotNull();
	}
	
	private Book createValidBook(String isbn) {
		Book book = new Book();
		book.setTitle("As aventuras");
		book.setAuthor("Fulano");
		book.setIsbn(isbn);
		return book;
	}
}
