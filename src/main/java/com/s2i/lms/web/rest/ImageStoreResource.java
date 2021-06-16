package com.s2i.lms.web.rest;

import com.s2i.lms.repository.ImageStoreRepository;
import com.s2i.lms.service.ImageStoreService;
import com.s2i.lms.service.dto.ImageStoreDTO;
import com.s2i.lms.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.s2i.lms.domain.ImageStore}.
 */
@RestController
@RequestMapping("/api")
public class ImageStoreResource {

    private final Logger log = LoggerFactory.getLogger(ImageStoreResource.class);

    private static final String ENTITY_NAME = "imageStore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageStoreService imageStoreService;

    private final ImageStoreRepository imageStoreRepository;

    public ImageStoreResource(ImageStoreService imageStoreService, ImageStoreRepository imageStoreRepository) {
        this.imageStoreService = imageStoreService;
        this.imageStoreRepository = imageStoreRepository;
    }

    /**
     * {@code POST  /image-stores} : Create a new imageStore.
     *
     * @param imageStoreDTO the imageStoreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imageStoreDTO, or with status {@code 400 (Bad Request)} if the imageStore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/image-stores")
    public ResponseEntity<ImageStoreDTO> createImageStore(@RequestBody ImageStoreDTO imageStoreDTO) throws URISyntaxException {
        log.debug("REST request to save ImageStore : {}", imageStoreDTO);
        if (imageStoreDTO.getId() != null) {
            throw new BadRequestAlertException("A new imageStore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ImageStoreDTO result = imageStoreService.save(imageStoreDTO);
        return ResponseEntity
            .created(new URI("/api/image-stores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /image-stores/:id} : Updates an existing imageStore.
     *
     * @param id the id of the imageStoreDTO to save.
     * @param imageStoreDTO the imageStoreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageStoreDTO,
     * or with status {@code 400 (Bad Request)} if the imageStoreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imageStoreDTO couldn"t be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/image-stores/{id}")
    public ResponseEntity<ImageStoreDTO> updateImageStore(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImageStoreDTO imageStoreDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ImageStore : {}, {}", id, imageStoreDTO);
        if (imageStoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageStoreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageStoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ImageStoreDTO result = imageStoreService.save(imageStoreDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageStoreDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /image-stores/:id} : Partial updates given fields of an existing imageStore, field will ignore if it is null
     *
     * @param id the id of the imageStoreDTO to save.
     * @param imageStoreDTO the imageStoreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageStoreDTO,
     * or with status {@code 400 (Bad Request)} if the imageStoreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the imageStoreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the imageStoreDTO couldn"t be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/image-stores/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ImageStoreDTO> partialUpdateImageStore(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImageStoreDTO imageStoreDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ImageStore partially : {}, {}", id, imageStoreDTO);
        if (imageStoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageStoreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageStoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImageStoreDTO> result = imageStoreService.partialUpdate(imageStoreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageStoreDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /image-stores} : get all the imageStores.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imageStores in body.
     */
    @GetMapping("/image-stores")
    public List<ImageStoreDTO> getAllImageStores() {
        log.debug("REST request to get all ImageStores");
        return imageStoreService.findAll();
    }

    /**
     * {@code GET  /image-stores/:id} : get the "id" imageStore.
     *
     * @param id the id of the imageStoreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imageStoreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/image-stores/{id}")
    public ResponseEntity<ImageStoreDTO> getImageStore(@PathVariable Long id) {
        log.debug("REST request to get ImageStore : {}", id);
        Optional<ImageStoreDTO> imageStoreDTO = imageStoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imageStoreDTO);
    }

    /**
     * {@code DELETE  /image-stores/:id} : delete the "id" imageStore.
     *
     * @param id the id of the imageStoreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/image-stores/{id}")
    public ResponseEntity<Void> deleteImageStore(@PathVariable Long id) {
        log.debug("REST request to delete ImageStore : {}", id);
        imageStoreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/image-stores/image/{id}")
    public ImageStoreDTO findImage(@PathVariable String id) {
        log.debug("REST request to get all ImageStores");

        return new ImageStoreDTO(1L, "test" + id, "JVBERi0xLjcKCjEgMCBvYmogICUgZW50cnkgcG9pbnQKPDwKICAvVHlwZSAvQ2F0YWxvZwog" +
        	    "IC9QYWdlcyAyIDAgUgo+PgplbmRvYmoKCjIgMCBvYmoKPDwKICAvVHlwZSAvUGFnZXMKICAv" +
        	    "TWVkaWFCb3ggWyAwIDAgMjAwIDIwMCBdCiAgL0NvdW50IDEKICAvS2lkcyBbIDMgMCBSIF0K" +
        	    "Pj4KZW5kb2JqCgozIDAgb2JqCjw8CiAgL1R5cGUgL1BhZ2UKICAvUGFyZW50IDIgMCBSCiAg" +
        	    "L1Jlc291cmNlcyA8PAogICAgL0ZvbnQgPDwKICAgICAgL0YxIDQgMCBSIAogICAgPj4KICA+" +
        	    "PgogIC9Db250ZW50cyA1IDAgUgo+PgplbmRvYmoKCjQgMCBvYmoKPDwKICAvVHlwZSAvRm9u" +
        	    "dAogIC9TdWJ0eXBlIC9UeXBlMQogIC9CYXNlRm9udCAvVGltZXMtUm9tYW4KPj4KZW5kb2Jq" +
        	    "Cgo1IDAgb2JqICAlIHBhZ2UgY29udGVudAo8PAogIC9MZW5ndGggNDQKPj4Kc3RyZWFtCkJU" +
        	    "CjcwIDUwIFRECi9GMSAxMiBUZgooSGVsbG8sIHdvcmxkISkgVGoKRVQKZW5kc3RyZWFtCmVu" +
        	    "ZG9iagoKeHJlZgowIDYKMDAwMDAwMDAwMCA2NTUzNSBmIAowMDAwMDAwMDEwIDAwMDAwIG4g" +
        	    "CjAwMDAwMDAwNzkgMDAwMDAgbiAKMDAwMDAwMDE3MyAwMDAwMCBuIAowMDAwMDAwMzAxIDAw" +
        	    "MDAwIG4gCjAwMDAwMDAzODAgMDAwMDAgbiAKdHJhaWxlcgo8PAogIC9TaXplIDYKICAvUm9v" +
        	    "dCAxIDAgUgo+PgpzdGFydHhyZWYKNDkyCiUlRU9G");
    }
}
