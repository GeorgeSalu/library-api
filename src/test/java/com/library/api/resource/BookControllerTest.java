package com.library.api.resource;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.api.dto.BookDTO;
import com.library.api.service.BookService;
import com.library.exception.BusinessException;
import com.library.model.entity.Book;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

	static String BOOK_API = "/api/books";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	BookService service;
	
	@Test
	@DisplayName("Deve criar um livro com sucesso.")
	public void createBookTest() throws Exception {
		
		BookDTO dto = createNewBook();
		
		Book savedBook = new Book();
		savedBook.setId(10l);
		savedBook.setAuthor("Artur");
		savedBook.setTitle("As aventuras");
		savedBook.setIsbn("001");
		
		BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
			.post(BOOK_API)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(json);
		
		mvc
			.perform(requestBuilder)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("id").isNotEmpty())
			.andExpect(jsonPath("title").value(dto.getTitle()))
			.andExpect(jsonPath("author").value(dto.getAuthor()))
			.andExpect(jsonPath("isbn").value(dto.getIsbn()));
		
	}
	
	@Test
	@DisplayName("Deve lançar erro de validação quando não houver dados suficientes para criação do livro.")
	public void createInvalidBookTest() throws Exception {
		
		String json = new ObjectMapper().writeValueAsString(new BookDTO());
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
			
		mvc
			.perform(requestBuilder)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("errors", hasSize(3)));
		
	}
	
	@Test
	@DisplayName("Deve lançar erro ao tentar cadastrar um livro com isbn já utilizado por outro.")
	public void createBookWithDuplicatedIsbn() throws Exception {
	
		BookDTO dto = createNewBook();
		String json = new ObjectMapper().writeValueAsString(dto);
		String mensagemErro = "Isbn ja cadastrado";
		BDDMockito.given(service.save(Mockito.any(Book.class)))
					.willThrow(new BusinessException(mensagemErro));
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
			
		mvc
			.perform(requestBuilder)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("errors", hasSize(1)))
			.andExpect(jsonPath("errors[0]").value(mensagemErro));
		
	}
	
	@Test
	@DisplayName("Deve obter informações de um livro")
	public void getBookDetailsTest() throws Exception {
		
		//cenario
		Long id = 1l;
		
		Book book = new Book();
		book.setId(id);
		book.setTitle("As aventuras");
		book.setAuthor("Artur");
		book.setIsbn("001");
		
		BDDMockito.given(service.getById(id)).willReturn(Optional.of(book));
		
		//execucao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.get(BOOK_API.concat("/"+id))
			.accept(MediaType.APPLICATION_JSON);
			
		mvc
			.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("id").value(id))
			.andExpect(jsonPath("title").value(book.getTitle()))
			.andExpect(jsonPath("author").value(book.getAuthor()))
			.andExpect(jsonPath("isbn").value(book.getIsbn()));
	}


	private BookDTO createNewBook() {
		BookDTO dto = new BookDTO();
		dto.setAuthor("Artur");
		dto.setTitle("As aventuras");
		dto.setIsbn("001");
		return dto;
	}
}














