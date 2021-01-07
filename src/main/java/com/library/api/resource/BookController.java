package com.library.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.library.api.dto.BookDTO;
import com.library.api.service.BookService;
import com.library.model.entity.Book;

@RestController
@RequestMapping("/api/books")
public class BookController {
	
	private BookService service;
	
	public BookController(BookService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public BookDTO create(@RequestBody BookDTO dto) {
		Book entity = new Book();
		entity.setAuthor(dto.getAuthor());
		entity.setTitle(dto.getTitle());
		entity.setIsbn(dto.getIsbn());
		
		entity = service.save(entity);
		
		return BookDTO.builder(entity);
	}
	
}
