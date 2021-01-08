package com.library.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.model.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

}