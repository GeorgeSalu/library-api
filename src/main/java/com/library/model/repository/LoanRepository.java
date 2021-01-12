package com.library.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.library.model.entity.Book;
import com.library.model.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long>{

	@Query(value = " select case when ( count(l.id) > 0 ) then true else false end "
			+ " from Loan l where l.book = :book and (l.returned is null or  l.returned is not true) ")
	boolean existsByBookAndNotReturned(@Param("book") Book book);

}