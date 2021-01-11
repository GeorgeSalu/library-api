package com.library.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.model.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long>{

}
