package com.jhipster.demo.expenses.domain;

import static com.jhipster.demo.expenses.domain.ExpensesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.demo.expenses.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpensesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Expenses.class);
        Expenses expenses1 = getExpensesSample1();
        Expenses expenses2 = new Expenses();
        assertThat(expenses1).isNotEqualTo(expenses2);

        expenses2.setId(expenses1.getId());
        assertThat(expenses1).isEqualTo(expenses2);

        expenses2 = getExpensesSample2();
        assertThat(expenses1).isNotEqualTo(expenses2);
    }
}
