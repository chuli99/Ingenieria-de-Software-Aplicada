package com.jhipster.demo.expenses.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jhipster.demo.expenses.domain.Expenses;
import com.jhipster.demo.expenses.repository.ExpensesRepository;
import com.jhipster.demo.expenses.service.dto.ExpensesDTO;
import com.jhipster.demo.expenses.service.mapper.ExpensesMapper;
import java.math.BigDecimal;
import java.time.Instant;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link ExpensesService}.
 */
@ExtendWith(MockitoExtension.class)
class ExpensesServiceTest {

    @Mock
    private ExpensesRepository expensesRepository;

    @Mock
    private ExpensesMapper expensesMapper;

    @InjectMocks
    private ExpensesService expensesService;

    private ExpensesDTO validExpensesDTO;
    private ExpensesDTO invalidExpensesDTO;
    private Expenses validExpenses;

    @BeforeEach
    void setUp() {
        // DTO válido con descripción
        validExpensesDTO = new ExpensesDTO();
        validExpensesDTO.setDescription("Gasto de prueba");
        validExpensesDTO.setAmount(new BigDecimal("100.50"));
        validExpensesDTO.setDate(Instant.now());

        // DTO inválido sin descripción
        invalidExpensesDTO = new ExpensesDTO();
        invalidExpensesDTO.setDescription(null); // Campo requerido es null
        invalidExpensesDTO.setAmount(new BigDecimal("100.50"));
        invalidExpensesDTO.setDate(Instant.now());

        // Entidad válida
        validExpenses = new Expenses();
        validExpenses.setId(1L);
        validExpenses.setDescription("Gasto de prueba");
        validExpenses.setAmount(new BigDecimal("100.50"));
        validExpenses.setDate(Instant.now());
    }

    @Test
    void shouldSaveExpenseSuccessfullyWhenValidData() {
        // Given
        when(expensesMapper.toEntity(validExpensesDTO)).thenReturn(validExpenses);
        when(expensesRepository.save(any(Expenses.class))).thenReturn(validExpenses);
        when(expensesMapper.toDto(validExpenses)).thenReturn(validExpensesDTO);

        // When
        ExpensesDTO result = expensesService.save(validExpensesDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Gasto de prueba");
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("100.50"));
        assertThat(result.getDate()).isNotNull();

        // Verificar que se llamaron los métodos correctos
        verify(expensesMapper, times(1)).toEntity(validExpensesDTO);
        verify(expensesRepository, times(1)).save(any(Expenses.class));
        verify(expensesMapper, times(1)).toDto(validExpenses);
    }

    @Test
    void shouldNotSaveExpenseWhenDescriptionIsNull() {
        // Given
        Expenses invalidExpenses = new Expenses();
        invalidExpenses.setDescription(null); // Campo requerido es null
        invalidExpenses.setAmount(new BigDecimal("100.50"));
        invalidExpenses.setDate(Instant.now());

        when(expensesMapper.toEntity(invalidExpensesDTO)).thenReturn(invalidExpenses);
        when(expensesRepository.save(any(Expenses.class))).thenThrow(new ConstraintViolationException("La descripción es requerida", null));

        // When & Then
        assertThatThrownBy(() -> expensesService.save(invalidExpensesDTO))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("La descripción es requerida");

        // Verificar que se intentó mapear la entidad pero falló al guardar
        verify(expensesMapper, times(1)).toEntity(invalidExpensesDTO);
        verify(expensesRepository, times(1)).save(any(Expenses.class));
        // El mapper no debe ser llamado para convertir a DTO porque falló el guardado
        verify(expensesMapper, times(0)).toDto(any(Expenses.class));
    }

    @Test
    void shouldNotSaveExpenseWhenDescriptionIsEmpty() {
        // Given
        ExpensesDTO emptyDescriptionDTO = new ExpensesDTO();
        emptyDescriptionDTO.setDescription(""); // Descripción vacía
        emptyDescriptionDTO.setAmount(new BigDecimal("100.50"));
        emptyDescriptionDTO.setDate(Instant.now());

        Expenses emptyDescriptionExpenses = new Expenses();
        emptyDescriptionExpenses.setDescription("");
        emptyDescriptionExpenses.setAmount(new BigDecimal("100.50"));
        emptyDescriptionExpenses.setDate(Instant.now());

        when(expensesMapper.toEntity(emptyDescriptionDTO)).thenReturn(emptyDescriptionExpenses);
        when(expensesRepository.save(any(Expenses.class))).thenThrow(
            new ConstraintViolationException("La descripción no puede estar vacía", null)
        );

        // When & Then
        assertThatThrownBy(() -> expensesService.save(emptyDescriptionDTO))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("La descripción no puede estar vacía");

        // Verificaciones
        verify(expensesMapper, times(1)).toEntity(emptyDescriptionDTO);
        verify(expensesRepository, times(1)).save(any(Expenses.class));
        verify(expensesMapper, times(0)).toDto(any(Expenses.class));
    }
}
