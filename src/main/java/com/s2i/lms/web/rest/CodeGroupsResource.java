package com.s2i.lms.web.rest;

import com.s2i.lms.repository.CodeGroupsRepository;
import com.s2i.lms.service.CodeGroupsService;
import com.s2i.lms.service.dto.CodeGroupsDTO;
import com.s2i.lms.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.s2i.lms.domain.CodeGroups}.
 */
@RestController
@RequestMapping("/api")
public class CodeGroupsResource {

    private final Logger log = LoggerFactory.getLogger(CodeGroupsResource.class);

    private static final String ENTITY_NAME = "codeGroups";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CodeGroupsService codeGroupsService;

    private final CodeGroupsRepository codeGroupsRepository;

    public CodeGroupsResource(CodeGroupsService codeGroupsService, CodeGroupsRepository codeGroupsRepository) {
        this.codeGroupsService = codeGroupsService;
        this.codeGroupsRepository = codeGroupsRepository;
    }

    /**
     * {@code POST  /code-groups} : Create a new codeGroups.
     *
     * @param codeGroupsDTO the codeGroupsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new codeGroupsDTO, or with status {@code 400 (Bad Request)} if the codeGroups has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/code-groups")
    public ResponseEntity<CodeGroupsDTO> createCodeGroups(@RequestBody CodeGroupsDTO codeGroupsDTO) throws URISyntaxException {
        log.debug("REST request to save CodeGroups : {}", codeGroupsDTO);
        if (codeGroupsDTO.getId() != null) {
            throw new BadRequestAlertException("A new codeGroups cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CodeGroupsDTO result = codeGroupsService.save(codeGroupsDTO);
        return ResponseEntity
            .created(new URI("/api/code-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /code-groups/:id} : Updates an existing codeGroups.
     *
     * @param id the id of the codeGroupsDTO to save.
     * @param codeGroupsDTO the codeGroupsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codeGroupsDTO,
     * or with status {@code 400 (Bad Request)} if the codeGroupsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the codeGroupsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/code-groups/{id}")
    public ResponseEntity<CodeGroupsDTO> updateCodeGroups(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CodeGroupsDTO codeGroupsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CodeGroups : {}, {}", id, codeGroupsDTO);
        if (codeGroupsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codeGroupsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeGroupsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CodeGroupsDTO result = codeGroupsService.save(codeGroupsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codeGroupsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /code-groups/:id} : Partial updates given fields of an existing codeGroups, field will ignore if it is null
     *
     * @param id the id of the codeGroupsDTO to save.
     * @param codeGroupsDTO the codeGroupsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codeGroupsDTO,
     * or with status {@code 400 (Bad Request)} if the codeGroupsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the codeGroupsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the codeGroupsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/code-groups/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CodeGroupsDTO> partialUpdateCodeGroups(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CodeGroupsDTO codeGroupsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CodeGroups partially : {}, {}", id, codeGroupsDTO);
        if (codeGroupsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codeGroupsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeGroupsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CodeGroupsDTO> result = codeGroupsService.partialUpdate(codeGroupsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codeGroupsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /code-groups} : get all the codeGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of codeGroups in body.
     */
    @GetMapping("/code-groups")
    public ResponseEntity<List<CodeGroupsDTO>> getAllCodeGroups(Pageable pageable) {
        log.debug("REST request to get a page of CodeGroups");
        Page<CodeGroupsDTO> page = codeGroupsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /code-groups/:id} : get the "id" codeGroups.
     *
     * @param id the id of the codeGroupsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the codeGroupsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/code-groups/{id}")
    public ResponseEntity<CodeGroupsDTO> getCodeGroups(@PathVariable Long id) {
        log.debug("REST request to get CodeGroups : {}", id);
        Optional<CodeGroupsDTO> codeGroupsDTO = codeGroupsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(codeGroupsDTO);
    }

    /**
     * {@code DELETE  /code-groups/:id} : delete the "id" codeGroups.
     *
     * @param id the id of the codeGroupsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/code-groups/{id}")
    public ResponseEntity<Void> deleteCodeGroups(@PathVariable Long id) {
        log.debug("REST request to delete CodeGroups : {}", id);
        codeGroupsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
