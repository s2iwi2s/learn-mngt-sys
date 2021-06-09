package com.s2i.lms.service;

import com.s2i.lms.service.dto.CodeGroupsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.s2i.lms.domain.CodeGroups}.
 */
public interface CodeGroupsService {
    /**
     * Save a codeGroups.
     *
     * @param codeGroupsDTO the entity to save.
     * @return the persisted entity.
     */
    CodeGroupsDTO save(CodeGroupsDTO codeGroupsDTO);

    /**
     * Partially updates a codeGroups.
     *
     * @param codeGroupsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CodeGroupsDTO> partialUpdate(CodeGroupsDTO codeGroupsDTO);

    /**
     * Get all the codeGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CodeGroupsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" codeGroups.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CodeGroupsDTO> findOne(Long id);

    /**
     * Delete the "id" codeGroups.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
