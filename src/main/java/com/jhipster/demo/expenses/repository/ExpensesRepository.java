package com.jhipster.demo.expenses.repository;

import com.jhipster.demo.expenses.domain.Expenses;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Expenses entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Long> {}
