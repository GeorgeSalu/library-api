package com.library.api.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.library.api.dto.BookDTO;
import com.library.api.dto.LoanDTO;
import com.library.api.dto.LoanFilterDTO;
import com.library.api.dto.ReturnedLoanDTO;
import com.library.api.service.BookService;
import com.library.api.service.LoanService;
import com.library.model.entity.Book;
import com.library.model.entity.Loan;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

	private LoanService loanService;
	private BookService bookService;
	private ModelMapper modelMapper;

	public LoanController(LoanService loanService, BookService bookService, ModelMapper modelMapper) {
		this.loanService = loanService;
		this.bookService = bookService;
		this.modelMapper = modelMapper;
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
	
	@PatchMapping("{id}")
	public void returnBook(
			@PathVariable Long id, 
			@RequestBody ReturnedLoanDTO dto) {
		Loan loan = loanService.getById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		loan.setReturned(dto.getReturned());
		
		loanService.update(loan);
	}
	
	@GetMapping
	public Page<LoanDTO> find(LoanFilterDTO dto, Pageable pageRequest) {
		Page<Loan> result = loanService.find(dto, pageRequest);
		List<LoanDTO> loans = result.getContent().stream().map(entity -> {
			Book book = entity.getBook();
			BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
			LoanDTO loanDTO = modelMapper.map(entity, LoanDTO.class);
			loanDTO.setBook(bookDTO);
			return loanDTO;
		}).collect(Collectors.toList());
		return new PageImpl<LoanDTO>(loans, pageRequest, result.getTotalElements());
	}
}
