package com.s2i.lms.service;

import com.s2i.lms.service.dto.ImageStoreDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.s2i.lms.domain.ImageStore}.
 */
public interface ImageStoreService {
    /**
     * Save a imageStore.
     *
     * @param imageStoreDTO the entity to save.
     * @return the persisted entity.
     */
    ImageStoreDTO save(ImageStoreDTO imageStoreDTO);

    /**
     * Partially updates a imageStore.
     *
     * @param imageStoreDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ImageStoreDTO> partialUpdate(ImageStoreDTO imageStoreDTO);

    /**
     * Get all the imageStores.
     *
     * @return the list of entities.
     */
    List<ImageStoreDTO> findAll();

    /**
     * Get the "id" imageStore.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ImageStoreDTO> findOne(Long id);

    /**
     * Delete the "id" imageStore.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
