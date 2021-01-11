package com.library.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.library.api.dto.LoanDTO;
import com.library.api.service.BookService;
import com.library.api.service.LoanService;
import com.library.model.entity.Book;
import com.library.model.entity.Loan;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

	private LoanService loanService;
	private BookService bookService;

	public LoanController(LoanService loanService, BookService bookService) {
		this.loanService = loanService;
		this.bookService = bookService;
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Long create(@RequestBody LoanDTO dto) {
		Book book = bookService.getBookByIsbn(dto.getIsbn())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn"));
		Loan entity = Loan.criaLoan(dto, book);
		entity = loanService.save(entity);
		return entity.getId();
	}
}
