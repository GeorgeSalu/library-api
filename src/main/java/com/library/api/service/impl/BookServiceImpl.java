package com.library.api.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.library.api.service.BookService;
import com.library.exception.BusinessException;
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
		if(repository.existsByIsbn(book.getIsbn())) {
			throw new BusinessException("Isbn ja cadastrado.");
		}
		return repository.save(book);
	}

	@Override
	public Optional<Book> getById(Long id) {
		return repository.findById(id);
	}

	@Override
	public void delete(Book book) {
		if(book == null || book.getId() == null) {
			throw new IllegalArgumentException("Book id cant be null");
		}
		this.repository.delete(book);
	}

	@Override
	public Book update(Book book) {
		if(book == null || book.getId() == null) {
			throw new IllegalArgumentException("Book id cant be null");
		}
		return this.repository.save(book);
	}

}
