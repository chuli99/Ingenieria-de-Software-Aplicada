package com.jhipster.demo.expenses.service.mapper;

import com.jhipster.demo.expenses.domain.Expenses;
import com.jhipster.demo.expenses.service.dto.ExpensesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Expenses} and its DTO {@link ExpensesDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExpensesMapper extends EntityMapper<ExpensesDTO, Expenses> {}
