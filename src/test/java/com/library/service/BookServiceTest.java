package com.library.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.library.api.service.BookService;
import com.library.model.entity.Book;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

	BookService service;

	@Test
	@DisplayName("Deve salvar um livro")
	public void saveBookTest() {
		//cenario
		Book book = new Book();
		book.setIsbn("123");
		book.setAuthor("Fulano");
		book.setTitle("As aventuras");
		
		//execucao
		Book savedBook = service.save(book);
		
		//verificacao
		Assertions.assertThat(savedBook.getId()).isNotNull();
		Assertions.assertThat(savedBook.getIsbn()).isEqualTo("123");
		Assertions.assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
		Assertions.assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
	}
}

















