package com.jhipster.demo.expenses.web.rest;

import static com.jhipster.demo.expenses.domain.ExpensesAsserts.*;
import static com.jhipster.demo.expenses.web.rest.TestUtil.createUpdateProxyForBean;
import static com.jhipster.demo.expenses.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhipster.demo.expenses.IntegrationTest;
import com.jhipster.demo.expenses.domain.Expenses;
import com.jhipster.demo.expenses.repository.ExpensesRepository;
import com.jhipster.demo.expenses.service.dto.ExpensesDTO;
import com.jhipster.demo.expenses.service.mapper.ExpensesMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ExpensesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpensesResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/expenses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExpensesRepository expensesRepository;

    @Autowired
    private ExpensesMapper expensesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpensesMockMvc;

    private Expenses expenses;

    private Expenses insertedExpenses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expenses createEntity() {
        return new Expenses().description(DEFAULT_DESCRIPTION).amount(DEFAULT_AMOUNT).date(DEFAULT_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expenses createUpdatedEntity() {
        return new Expenses().description(UPDATED_DESCRIPTION).amount(UPDATED_AMOUNT).date(UPDATED_DATE);
    }

    @BeforeEach
    public void initTest() {
        expenses = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedExpenses != null) {
            expensesRepository.delete(insertedExpenses);
            insertedExpenses = null;
        }
    }

    @Test
    @Transactional
    void createExpenses() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Expenses
        ExpensesDTO expensesDTO = expensesMapper.toDto(expenses);
        var returnedExpensesDTO = om.readValue(
            restExpensesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expensesDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExpensesDTO.class
        );

        // Validate the Expenses in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExpenses = expensesMapper.toEntity(returnedExpensesDTO);
        assertExpensesUpdatableFieldsEquals(returnedExpenses, getPersistedExpenses(returnedExpenses));

        insertedExpenses = returnedExpenses;
    }

    @Test
    @Transactional
    void createExpensesWithExistingId() throws Exception {
        // Create the Expenses with an existing ID
        expenses.setId(1L);
        ExpensesDTO expensesDTO = expensesMapper.toDto(expenses);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpensesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expensesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Expenses in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        expenses.setDescription(null);

        // Create the Expenses, which fails.
        ExpensesDTO expensesDTO = expensesMapper.toDto(expenses);

        restExpensesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expensesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        expenses.setAmount(null);

        // Create the Expenses, which fails.
        ExpensesDTO expensesDTO = expensesMapper.toDto(expenses);

        restExpensesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expensesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        expenses.setDate(null);

        // Create the Expenses, which fails.
        ExpensesDTO expensesDTO = expensesMapper.toDto(expenses);

        restExpensesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expensesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExpenses() throws Exception {
        // Initialize the database
        insertedExpenses = expensesRepository.saveAndFlush(expenses);

        // Get all the expensesList
        restExpensesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenses.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getExpenses() throws Exception {
        // Initialize the database
        insertedExpenses = expensesRepository.saveAndFlush(expenses);

        // Get the expenses
        restExpensesMockMvc
            .perform(get(ENTITY_API_URL_ID, expenses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expenses.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExpenses() throws Exception {
        // Get the expenses
        restExpensesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExpenses() throws Exception {
        // Initialize the database
        insertedExpenses = expensesRepository.saveAndFlush(expenses);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expenses
        Expenses updatedExpenses = expensesRepository.findById(expenses.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExpenses are not directly saved in db
        em.detach(updatedExpenses);
        updatedExpenses.description(UPDATED_DESCRIPTION).amount(UPDATED_AMOUNT).date(UPDATED_DATE);
        ExpensesDTO expensesDTO = expensesMapper.toDto(updatedExpenses);

        restExpensesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expensesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expensesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Expenses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExpensesToMatchAllProperties(updatedExpenses);
    }

    @Test
    @Transactional
    void putNonExistingExpenses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenses.setId(longCount.incrementAndGet());

        // Create the Expenses
        ExpensesDTO expensesDTO = expensesMapper.toDto(expenses);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpensesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expensesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expensesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expenses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExpenses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenses.setId(longCount.incrementAndGet());

        // Create the Expenses
        ExpensesDTO expensesDTO = expensesMapper.toDto(expenses);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpensesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expensesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expenses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExpenses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenses.setId(longCount.incrementAndGet());

        // Create the Expenses
        ExpensesDTO expensesDTO = expensesMapper.toDto(expenses);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpensesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expensesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Expenses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExpensesWithPatch() throws Exception {
        // Initialize the database
        insertedExpenses = expensesRepository.saveAndFlush(expenses);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expenses using partial update
        Expenses partialUpdatedExpenses = new Expenses();
        partialUpdatedExpenses.setId(expenses.getId());

        partialUpdatedExpenses.amount(UPDATED_AMOUNT);

        restExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenses.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExpenses))
            )
            .andExpect(status().isOk());

        // Validate the Expenses in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExpensesUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedExpenses, expenses), getPersistedExpenses(expenses));
    }

    @Test
    @Transactional
    void fullUpdateExpensesWithPatch() throws Exception {
        // Initialize the database
        insertedExpenses = expensesRepository.saveAndFlush(expenses);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expenses using partial update
        Expenses partialUpdatedExpenses = new Expenses();
        partialUpdatedExpenses.setId(expenses.getId());

        partialUpdatedExpenses.description(UPDATED_DESCRIPTION).amount(UPDATED_AMOUNT).date(UPDATED_DATE);

        restExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenses.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExpenses))
            )
            .andExpect(status().isOk());

        // Validate the Expenses in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExpensesUpdatableFieldsEquals(partialUpdatedExpenses, getPersistedExpenses(partialUpdatedExpenses));
    }

    @Test
    @Transactional
    void patchNonExistingExpenses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenses.setId(longCount.incrementAndGet());

        // Create the Expenses
        ExpensesDTO expensesDTO = expensesMapper.toDto(expenses);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expensesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(expensesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expenses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExpenses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenses.setId(longCount.incrementAndGet());

        // Create the Expenses
        ExpensesDTO expensesDTO = expensesMapper.toDto(expenses);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(expensesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expenses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExpenses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenses.setId(longCount.incrementAndGet());

        // Create the Expenses
        ExpensesDTO expensesDTO = expensesMapper.toDto(expenses);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpensesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(expensesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Expenses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExpenses() throws Exception {
        // Initialize the database
        insertedExpenses = expensesRepository.saveAndFlush(expenses);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the expenses
        restExpensesMockMvc
            .perform(delete(ENTITY_API_URL_ID, expenses.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return expensesRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Expenses getPersistedExpenses(Expenses expenses) {
        return expensesRepository.findById(expenses.getId()).orElseThrow();
    }

    protected void assertPersistedExpensesToMatchAllProperties(Expenses expectedExpenses) {
        assertExpensesAllPropertiesEquals(expectedExpenses, getPersistedExpenses(expectedExpenses));
    }

    protected void assertPersistedExpensesToMatchUpdatableProperties(Expenses expectedExpenses) {
        assertExpensesAllUpdatablePropertiesEquals(expectedExpenses, getPersistedExpenses(expectedExpenses));
    }
}
