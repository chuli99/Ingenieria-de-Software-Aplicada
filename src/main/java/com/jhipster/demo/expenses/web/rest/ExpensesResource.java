package com.jhipster.demo.expenses.web.rest;

import com.jhipster.demo.expenses.repository.ExpensesRepository;
import com.jhipster.demo.expenses.service.ExpensesService;
import com.jhipster.demo.expenses.service.dto.ExpensesDTO;
import com.jhipster.demo.expenses.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.jhipster.demo.expenses.domain.Expenses}.
 */
@RestController
@RequestMapping("/api/expenses")
public class ExpensesResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExpensesResource.class);

    private static final String ENTITY_NAME = "expenses";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpensesService expensesService;

    private final ExpensesRepository expensesRepository;

    public ExpensesResource(ExpensesService expensesService, ExpensesRepository expensesRepository) {
        this.expensesService = expensesService;
        this.expensesRepository = expensesRepository;
    }

    /**
     * {@code POST  /expenses} : Create a new expenses.
     *
     * @param expensesDTO the expensesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expensesDTO, or with status {@code 400 (Bad Request)} if the expenses has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExpensesDTO> createExpenses(@Valid @RequestBody ExpensesDTO expensesDTO) throws URISyntaxException {
        LOG.debug("REST request to save Expenses : {}", expensesDTO);
        if (expensesDTO.getId() != null) {
            throw new BadRequestAlertException("A new expenses cannot already have an ID", ENTITY_NAME, "idexists");
        }
        expensesDTO = expensesService.save(expensesDTO);
        return ResponseEntity.created(new URI("/api/expenses/" + expensesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, expensesDTO.getId().toString()))
            .body(expensesDTO);
    }

    /**
     * {@code PUT  /expenses/:id} : Updates an existing expenses.
     *
     * @param id the id of the expensesDTO to save.
     * @param expensesDTO the expensesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expensesDTO,
     * or with status {@code 400 (Bad Request)} if the expensesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expensesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExpensesDTO> updateExpenses(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExpensesDTO expensesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Expenses : {}, {}", id, expensesDTO);
        if (expensesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expensesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expensesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        expensesDTO = expensesService.update(expensesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expensesDTO.getId().toString()))
            .body(expensesDTO);
    }

    /**
     * {@code PATCH  /expenses/:id} : Partial updates given fields of an existing expenses, field will ignore if it is null
     *
     * @param id the id of the expensesDTO to save.
     * @param expensesDTO the expensesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expensesDTO,
     * or with status {@code 400 (Bad Request)} if the expensesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the expensesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the expensesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExpensesDTO> partialUpdateExpenses(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExpensesDTO expensesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Expenses partially : {}, {}", id, expensesDTO);
        if (expensesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expensesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expensesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExpensesDTO> result = expensesService.partialUpdate(expensesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expensesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /expenses} : get all the expenses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expenses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExpensesDTO>> getAllExpenses(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Expenses");
        Page<ExpensesDTO> page = expensesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /expenses/:id} : get the "id" expenses.
     *
     * @param id the id of the expensesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expensesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpensesDTO> getExpenses(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Expenses : {}", id);
        Optional<ExpensesDTO> expensesDTO = expensesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expensesDTO);
    }

    /**
     * {@code DELETE  /expenses/:id} : delete the "id" expenses.
     *
     * @param id the id of the expensesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenses(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Expenses : {}", id);
        expensesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
