package com.library.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.library.api.service.BookService;
import com.library.api.service.impl.BookServiceImpl;
import com.library.exception.BusinessException;
import com.library.model.entity.Book;
import com.library.model.repository.BookRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

	BookService service;
	@MockBean
	private BookRepository repository;
	
	@BeforeEach
	public void setUp() {
		this.service = new BookServiceImpl(repository);
	}

	@Test
	@DisplayName("Deve salvar um livro")
	public void saveBookTest() {
		//cenario
		Book book = createValidBook();
		
		Book bookReturn = new Book();
		bookReturn.setId(1l);
		bookReturn.setIsbn("123");
		bookReturn.setAuthor("Fulano");
		bookReturn.setTitle("As aventuras");

		Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
		Mockito.when(repository.save(book)).thenReturn(bookReturn);
		
		//execucao
		Book savedBook = service.save(book);
		
		//verificacao
		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getIsbn()).isEqualTo("123");
		assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
		assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
	}

	@Test
	@DisplayName("Deve lancar erro de nogocio ao tentar salvar um livro com isbn duplicado.")
	public void shouldNotSaveABookWithDuplicatedISBN() {
		//cenario
		Book book = createValidBook();
		Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
		
		//execucao
		Throwable exception = Assertions.catchThrowable(() -> service.save(book));
		
		//verificacoes
		assertThat(exception)
			.isInstanceOf(BusinessException.class)
			.hasMessage("Isbn ja cadastrado.");

		Mockito.verify(repository, Mockito.never()).save(book);
	}

	private Book createValidBook() {
		Book book = new Book();
		book.setIsbn("123");
		book.setAuthor("Fulano");
		book.setTitle("As aventuras");
		return book;
	}
}

















