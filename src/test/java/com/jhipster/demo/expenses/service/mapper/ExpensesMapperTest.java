package com.jhipster.demo.expenses.service.mapper;

import static com.jhipster.demo.expenses.domain.ExpensesAsserts.*;
import static com.jhipster.demo.expenses.domain.ExpensesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExpensesMapperTest {

    private ExpensesMapper expensesMapper;

    @BeforeEach
    void setUp() {
        expensesMapper = new ExpensesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExpensesSample1();
        var actual = expensesMapper.toEntity(expensesMapper.toDto(expected));
        assertExpensesAllPropertiesEquals(expected, actual);
    }
}
