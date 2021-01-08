package com.library.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

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

	@Test
	@DisplayName("Deve obter um livro por id")
	public void getByIdTest() {
		Long id = 1l;
		Book book = createValidBook();
		book.setId(id);
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));
		
		//execucao
		Optional<Book> foundBook = service.getById(id);
		
		//verificacoes
		Assertions.assertThat(foundBook.isPresent()).isTrue();
		Assertions.assertThat(foundBook.get().getId()).isEqualTo(id);
		Assertions.assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
		Assertions.assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
		Assertions.assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
	}
	
	@Test
	@DisplayName("Deve retornar vazio ao obter um livro por id quando ele não existe na base.")
	public void bookNotFoundByIdTest() {
		Long id = 1l;
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
		//execucao
		Optional<Book> book = service.getById(id);
		
		//verificacoes
		Assertions.assertThat(book.isPresent()).isFalse();
	}
	
	@Test
	@DisplayName("Deve deletar um livro")
	public void deleteBookTest() {
		
		Book book = new Book();
		book.setId(1l);
		
		//execucao
		org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.delete(book));
		
		//verificacao
		Mockito.verify(repository, Mockito.times(1)).delete(book);
	}
	
	@Test
	@DisplayName("Deve ocorrer erro ao tentar deletar um livro inexistente")
	public void deleteInvalidBookTest() {
		Book book = new Book();
		
		org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(book));

		Mockito.verify(repository, Mockito.never()).delete(book);
	}
	
	private Book createValidBook() {
		Book book = new Book();
		book.setIsbn("123");
		book.setAuthor("Fulano");
		book.setTitle("As aventuras");
		return book;
	}
}

















