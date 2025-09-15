package com.jhipster.demo.expenses.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.jhipster.demo.expenses.domain.Expenses} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExpensesDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String description;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Instant date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExpensesDTO)) {
            return false;
        }

        ExpensesDTO expensesDTO = (ExpensesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, expensesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpensesDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
