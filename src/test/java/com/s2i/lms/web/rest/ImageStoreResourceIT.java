package com.s2i.lms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.s2i.lms.IntegrationTest;
import com.s2i.lms.domain.ImageStore;
import com.s2i.lms.repository.ImageStoreRepository;
import com.s2i.lms.service.dto.ImageStoreDTO;
import com.s2i.lms.service.mapper.ImageStoreMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ImageStoreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImageStoreResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_STORE = "AAAAAAAAAA";
    private static final String UPDATED_STORE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/image-stores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImageStoreRepository imageStoreRepository;

    @Autowired
    private ImageStoreMapper imageStoreMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageStoreMockMvc;

    private ImageStore imageStore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageStore createEntity(EntityManager em) {
        ImageStore imageStore = new ImageStore().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).store(DEFAULT_STORE);
        return imageStore;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageStore createUpdatedEntity(EntityManager em) {
        ImageStore imageStore = new ImageStore().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).store(UPDATED_STORE);
        return imageStore;
    }

    @BeforeEach
    public void initTest() {
        imageStore = createEntity(em);
    }

    @Test
    @Transactional
    void createImageStore() throws Exception {
        int databaseSizeBeforeCreate = imageStoreRepository.findAll().size();
        // Create the ImageStore
        ImageStoreDTO imageStoreDTO = imageStoreMapper.toDto(imageStore);
        restImageStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageStoreDTO)))
            .andExpect(status().isCreated());

        // Validate the ImageStore in the database
        List<ImageStore> imageStoreList = imageStoreRepository.findAll();
        assertThat(imageStoreList).hasSize(databaseSizeBeforeCreate + 1);
        ImageStore testImageStore = imageStoreList.get(imageStoreList.size() - 1);
        assertThat(testImageStore.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testImageStore.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testImageStore.getStore()).isEqualTo(DEFAULT_STORE);
    }

    @Test
    @Transactional
    void createImageStoreWithExistingId() throws Exception {
        // Create the ImageStore with an existing ID
        imageStore.setId(1L);
        ImageStoreDTO imageStoreDTO = imageStoreMapper.toDto(imageStore);

        int databaseSizeBeforeCreate = imageStoreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageStoreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ImageStore in the database
        List<ImageStore> imageStoreList = imageStoreRepository.findAll();
        assertThat(imageStoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllImageStores() throws Exception {
        // Initialize the database
        imageStoreRepository.saveAndFlush(imageStore);

        // Get all the imageStoreList
        restImageStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageStore.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].store").value(hasItem(DEFAULT_STORE)));
    }

    @Test
    @Transactional
    void getImageStore() throws Exception {
        // Initialize the database
        imageStoreRepository.saveAndFlush(imageStore);

        // Get the imageStore
        restImageStoreMockMvc
            .perform(get(ENTITY_API_URL_ID, imageStore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imageStore.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.store").value(DEFAULT_STORE));
    }

    @Test
    @Transactional
    void getNonExistingImageStore() throws Exception {
        // Get the imageStore
        restImageStoreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewImageStore() throws Exception {
        // Initialize the database
        imageStoreRepository.saveAndFlush(imageStore);

        int databaseSizeBeforeUpdate = imageStoreRepository.findAll().size();

        // Update the imageStore
        ImageStore updatedImageStore = imageStoreRepository.findById(imageStore.getId()).get();
        // Disconnect from session so that the updates on updatedImageStore are not directly saved in db
        em.detach(updatedImageStore);
        updatedImageStore.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).store(UPDATED_STORE);
        ImageStoreDTO imageStoreDTO = imageStoreMapper.toDto(updatedImageStore);

        restImageStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageStoreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageStoreDTO))
            )
            .andExpect(status().isOk());

        // Validate the ImageStore in the database
        List<ImageStore> imageStoreList = imageStoreRepository.findAll();
        assertThat(imageStoreList).hasSize(databaseSizeBeforeUpdate);
        ImageStore testImageStore = imageStoreList.get(imageStoreList.size() - 1);
        assertThat(testImageStore.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testImageStore.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testImageStore.getStore()).isEqualTo(UPDATED_STORE);
    }

    @Test
    @Transactional
    void putNonExistingImageStore() throws Exception {
        int databaseSizeBeforeUpdate = imageStoreRepository.findAll().size();
        imageStore.setId(count.incrementAndGet());

        // Create the ImageStore
        ImageStoreDTO imageStoreDTO = imageStoreMapper.toDto(imageStore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageStoreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageStore in the database
        List<ImageStore> imageStoreList = imageStoreRepository.findAll();
        assertThat(imageStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImageStore() throws Exception {
        int databaseSizeBeforeUpdate = imageStoreRepository.findAll().size();
        imageStore.setId(count.incrementAndGet());

        // Create the ImageStore
        ImageStoreDTO imageStoreDTO = imageStoreMapper.toDto(imageStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageStore in the database
        List<ImageStore> imageStoreList = imageStoreRepository.findAll();
        assertThat(imageStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImageStore() throws Exception {
        int databaseSizeBeforeUpdate = imageStoreRepository.findAll().size();
        imageStore.setId(count.incrementAndGet());

        // Create the ImageStore
        ImageStoreDTO imageStoreDTO = imageStoreMapper.toDto(imageStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageStoreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageStoreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageStore in the database
        List<ImageStore> imageStoreList = imageStoreRepository.findAll();
        assertThat(imageStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImageStoreWithPatch() throws Exception {
        // Initialize the database
        imageStoreRepository.saveAndFlush(imageStore);

        int databaseSizeBeforeUpdate = imageStoreRepository.findAll().size();

        // Update the imageStore using partial update
        ImageStore partialUpdatedImageStore = new ImageStore();
        partialUpdatedImageStore.setId(imageStore.getId());

        partialUpdatedImageStore.description(UPDATED_DESCRIPTION).store(UPDATED_STORE);

        restImageStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageStore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImageStore))
            )
            .andExpect(status().isOk());

        // Validate the ImageStore in the database
        List<ImageStore> imageStoreList = imageStoreRepository.findAll();
        assertThat(imageStoreList).hasSize(databaseSizeBeforeUpdate);
        ImageStore testImageStore = imageStoreList.get(imageStoreList.size() - 1);
        assertThat(testImageStore.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testImageStore.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testImageStore.getStore()).isEqualTo(UPDATED_STORE);
    }

    @Test
    @Transactional
    void fullUpdateImageStoreWithPatch() throws Exception {
        // Initialize the database
        imageStoreRepository.saveAndFlush(imageStore);

        int databaseSizeBeforeUpdate = imageStoreRepository.findAll().size();

        // Update the imageStore using partial update
        ImageStore partialUpdatedImageStore = new ImageStore();
        partialUpdatedImageStore.setId(imageStore.getId());

        partialUpdatedImageStore.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).store(UPDATED_STORE);

        restImageStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageStore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImageStore))
            )
            .andExpect(status().isOk());

        // Validate the ImageStore in the database
        List<ImageStore> imageStoreList = imageStoreRepository.findAll();
        assertThat(imageStoreList).hasSize(databaseSizeBeforeUpdate);
        ImageStore testImageStore = imageStoreList.get(imageStoreList.size() - 1);
        assertThat(testImageStore.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testImageStore.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testImageStore.getStore()).isEqualTo(UPDATED_STORE);
    }

    @Test
    @Transactional
    void patchNonExistingImageStore() throws Exception {
        int databaseSizeBeforeUpdate = imageStoreRepository.findAll().size();
        imageStore.setId(count.incrementAndGet());

        // Create the ImageStore
        ImageStoreDTO imageStoreDTO = imageStoreMapper.toDto(imageStore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imageStoreDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imageStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageStore in the database
        List<ImageStore> imageStoreList = imageStoreRepository.findAll();
        assertThat(imageStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImageStore() throws Exception {
        int databaseSizeBeforeUpdate = imageStoreRepository.findAll().size();
        imageStore.setId(count.incrementAndGet());

        // Create the ImageStore
        ImageStoreDTO imageStoreDTO = imageStoreMapper.toDto(imageStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imageStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageStore in the database
        List<ImageStore> imageStoreList = imageStoreRepository.findAll();
        assertThat(imageStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImageStore() throws Exception {
        int databaseSizeBeforeUpdate = imageStoreRepository.findAll().size();
        imageStore.setId(count.incrementAndGet());

        // Create the ImageStore
        ImageStoreDTO imageStoreDTO = imageStoreMapper.toDto(imageStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageStoreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(imageStoreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageStore in the database
        List<ImageStore> imageStoreList = imageStoreRepository.findAll();
        assertThat(imageStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImageStore() throws Exception {
        // Initialize the database
        imageStoreRepository.saveAndFlush(imageStore);

        int databaseSizeBeforeDelete = imageStoreRepository.findAll().size();

        // Delete the imageStore
        restImageStoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, imageStore.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ImageStore> imageStoreList = imageStoreRepository.findAll();
        assertThat(imageStoreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
