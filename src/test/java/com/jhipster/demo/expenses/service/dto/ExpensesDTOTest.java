package com.jhipster.demo.expenses.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.demo.expenses.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpensesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpensesDTO.class);
        ExpensesDTO expensesDTO1 = new ExpensesDTO();
        expensesDTO1.setId(1L);
        ExpensesDTO expensesDTO2 = new ExpensesDTO();
        assertThat(expensesDTO1).isNotEqualTo(expensesDTO2);
        expensesDTO2.setId(expensesDTO1.getId());
        assertThat(expensesDTO1).isEqualTo(expensesDTO2);
        expensesDTO2.setId(2L);
        assertThat(expensesDTO1).isNotEqualTo(expensesDTO2);
        expensesDTO1.setId(null);
        assertThat(expensesDTO1).isNotEqualTo(expensesDTO2);
    }
}
