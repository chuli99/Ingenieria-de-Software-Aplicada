package com.jhipster.demo.expenses.service;

import com.jhipster.demo.expenses.domain.Expenses;
import com.jhipster.demo.expenses.repository.ExpensesRepository;
import com.jhipster.demo.expenses.service.dto.ExpensesDTO;
import com.jhipster.demo.expenses.service.mapper.ExpensesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.jhipster.demo.expenses.domain.Expenses}.
 */
@Service
@Transactional
public class ExpensesService {

    private static final Logger LOG = LoggerFactory.getLogger(ExpensesService.class);

    private final ExpensesRepository expensesRepository;

    private final ExpensesMapper expensesMapper;

    public ExpensesService(ExpensesRepository expensesRepository, ExpensesMapper expensesMapper) {
        this.expensesRepository = expensesRepository;
        this.expensesMapper = expensesMapper;
    }

    /**
     * Save a expenses.
     *
     * @param expensesDTO the entity to save.
     * @return the persisted entity.
     */
    public ExpensesDTO save(ExpensesDTO expensesDTO) {
        LOG.debug("Request to save Expenses : {}", expensesDTO);
        Expenses expenses = expensesMapper.toEntity(expensesDTO);
        expenses = expensesRepository.save(expenses);
        return expensesMapper.toDto(expenses);
    }

    /**
     * Update a expenses.
     *
     * @param expensesDTO the entity to save.
     * @return the persisted entity.
     */
    public ExpensesDTO update(ExpensesDTO expensesDTO) {
        LOG.debug("Request to update Expenses : {}", expensesDTO);
        Expenses expenses = expensesMapper.toEntity(expensesDTO);
        expenses = expensesRepository.save(expenses);
        return expensesMapper.toDto(expenses);
    }

    /**
     * Partially update a expenses.
     *
     * @param expensesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExpensesDTO> partialUpdate(ExpensesDTO expensesDTO) {
        LOG.debug("Request to partially update Expenses : {}", expensesDTO);

        return expensesRepository
            .findById(expensesDTO.getId())
            .map(existingExpenses -> {
                expensesMapper.partialUpdate(existingExpenses, expensesDTO);

                return existingExpenses;
            })
            .map(expensesRepository::save)
            .map(expensesMapper::toDto);
    }

    /**
     * Get all the expenses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpensesDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Expenses");
        return expensesRepository.findAll(pageable).map(expensesMapper::toDto);
    }

    /**
     * Get one expenses by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExpensesDTO> findOne(Long id) {
        LOG.debug("Request to get Expenses : {}", id);
        return expensesRepository.findById(id).map(expensesMapper::toDto);
    }

    /**
     * Delete the expenses by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Expenses : {}", id);
        expensesRepository.deleteById(id);
    }
}
