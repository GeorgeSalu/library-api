package com.library.api.resource;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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
import com.library.api.dto.LoanDTO;
import com.library.api.dto.ReturnedLoanDTO;
import com.library.api.service.BookService;
import com.library.api.service.LoanService;
import com.library.exception.BusinessException;
import com.library.model.entity.Book;
import com.library.model.entity.Loan;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(controllers = LoanController.class)
public class LoanControllerTest {

	static final String LOAN_API = "/api/loans";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	BookService bookService;
	
	@MockBean
	LoanService loadService;
	
	
	@Test
	@DisplayName("Deve realizar um emprestimo.")
	public void createLoanTest() throws Exception {
		//cenario
		LoanDTO dto = new LoanDTO();
		dto.setIsbn("123");
		dto.setCustomer("Fulano");
		
		Book book = new Book();
		book.setId(1l);
		book.setIsbn("123");
		
		Loan loan = new Loan();
		loan.setId(1l);
		loan.setCustomer("Fulano");
		loan.setBook(book);
		loan.setLoanDate(LocalDate.now());
		
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//execucao
		BDDMockito.given(bookService.getBookByIsbn("123")).willReturn(Optional.of(book));
		BDDMockito.given(loadService.save(Mockito.any(Loan.class))).willReturn(loan);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json);
		
		//verificacao
		mvc.perform(request)
			.andExpect(status().isCreated())
			.andExpect(content().string("1"));
	}
	
	@Test
	@DisplayName("Deve retornar erro ao tentar fazer emprestimo de um livro inexistente.")
	public void invalidIsbnCreadetLoanTest() throws Exception {
		//cenario
		LoanDTO dto = new LoanDTO();
		dto.setIsbn("123");
		dto.setCustomer("Fulano");
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//execucao
		BDDMockito.given(bookService.getBookByIsbn("123")).willReturn(Optional.empty());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		//verificacao
		mvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("errors", hasSize(1)))
			.andExpect(jsonPath("errors[0]").value("Book not found for passed isbn"));
	
	}
	
	@Test
	@DisplayName("Deve retornar erro ao tentar fazer emprestimo de um livro empresatado.")
	public void loanedBookErrorOnCreatedLoanTest() throws Exception {
		//cenario
		Book book = new Book();
		book.setId(1l);
		book.setIsbn("123");
		
		LoanDTO dto = new LoanDTO();
		dto.setIsbn("123");
		dto.setCustomer("Fulano");
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//execucao
		BDDMockito.given(bookService.getBookByIsbn("123")).willReturn(Optional.of(book));
		
		BDDMockito.given(loadService.save(Mockito.any(Loan.class)))
			.willThrow(new BusinessException("Book already loaned"));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		//verificacao
		mvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("errors", hasSize(1)))
			.andExpect(jsonPath("errors[0]").value("Book already loaned"));
	
	}
	
	@Test
	@DisplayName("Deve retornar um livro")
	public void returnBookTest() throws Exception {
		// cenario { returned: true }
		ReturnedLoanDTO dto = new ReturnedLoanDTO();
		dto.setReturned(true);
		
		Loan loan = new Loan();
		loan.setId(1l);
		
		BDDMockito.given(loadService.getById(Mockito.anyLong())).willReturn(Optional.of(loan));
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		mvc.perform(
			patch(LOAN_API.concat("/1"))
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json)
		).andExpect(status().isOk());
	}
}







