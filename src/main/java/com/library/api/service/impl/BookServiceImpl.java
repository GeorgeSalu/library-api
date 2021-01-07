package com.library.api.service.impl;

import org.springframework.stereotype.Service;

import com.library.api.service.BookService;
import com.library.model.entity.Book;
import com.library.model.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

	private BookRepository repository;
	
	public BookServiceImpl(BookRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public Book save(Book book) {
		return repository.save(book);
	}

}
