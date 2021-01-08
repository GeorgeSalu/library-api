package com.library.api.service;

import java.util.Optional;

import com.library.model.entity.Book;

public interface BookService {

	Book save(Book any);

	Optional<Book> getById(Long id);

}
