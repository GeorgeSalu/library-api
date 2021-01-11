package com.library.api.resource;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.api.dto.LoanDTO;
import com.library.api.service.BookService;
import com.library.api.service.LoanService;
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
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(1l));
	}
	
}
