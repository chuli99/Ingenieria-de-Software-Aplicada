package com.jhipster.demo.expenses.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExpensesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Expenses getExpensesSample1() {
        return new Expenses().id(1L).description("description1");
    }

    public static Expenses getExpensesSample2() {
        return new Expenses().id(2L).description("description2");
    }

    public static Expenses getExpensesRandomSampleGenerator() {
        return new Expenses().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
