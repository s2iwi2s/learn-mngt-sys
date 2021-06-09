package com.s2i.lms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.s2i.lms.IntegrationTest;
import com.s2i.lms.domain.CodeGroups;
import com.s2i.lms.repository.CodeGroupsRepository;
import com.s2i.lms.service.dto.CodeGroupsDTO;
import com.s2i.lms.service.mapper.CodeGroupsMapper;
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
 * Integration tests for the {@link CodeGroupsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CodeGroupsResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_JSON = "AAAAAAAAAA";
    private static final String UPDATED_JSON = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final String ENTITY_API_URL = "/api/code-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CodeGroupsRepository codeGroupsRepository;

    @Autowired
    private CodeGroupsMapper codeGroupsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCodeGroupsMockMvc;

    private CodeGroups codeGroups;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodeGroups createEntity(EntityManager em) {
        CodeGroups codeGroups = new CodeGroups()
            .code(DEFAULT_CODE)
            .value(DEFAULT_VALUE)
            .description(DEFAULT_DESCRIPTION)
            .json(DEFAULT_JSON)
            .priority(DEFAULT_PRIORITY);
        return codeGroups;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodeGroups createUpdatedEntity(EntityManager em) {
        CodeGroups codeGroups = new CodeGroups()
            .code(UPDATED_CODE)
            .value(UPDATED_VALUE)
            .description(UPDATED_DESCRIPTION)
            .json(UPDATED_JSON)
            .priority(UPDATED_PRIORITY);
        return codeGroups;
    }

    @BeforeEach
    public void initTest() {
        codeGroups = createEntity(em);
    }

    @Test
    @Transactional
    void createCodeGroups() throws Exception {
        int databaseSizeBeforeCreate = codeGroupsRepository.findAll().size();
        // Create the CodeGroups
        CodeGroupsDTO codeGroupsDTO = codeGroupsMapper.toDto(codeGroups);
        restCodeGroupsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeGroupsDTO)))
            .andExpect(status().isCreated());

        // Validate the CodeGroups in the database
        List<CodeGroups> codeGroupsList = codeGroupsRepository.findAll();
        assertThat(codeGroupsList).hasSize(databaseSizeBeforeCreate + 1);
        CodeGroups testCodeGroups = codeGroupsList.get(codeGroupsList.size() - 1);
        assertThat(testCodeGroups.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCodeGroups.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testCodeGroups.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCodeGroups.getJson()).isEqualTo(DEFAULT_JSON);
        assertThat(testCodeGroups.getPriority()).isEqualTo(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void createCodeGroupsWithExistingId() throws Exception {
        // Create the CodeGroups with an existing ID
        codeGroups.setId(1L);
        CodeGroupsDTO codeGroupsDTO = codeGroupsMapper.toDto(codeGroups);

        int databaseSizeBeforeCreate = codeGroupsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodeGroupsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeGroupsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CodeGroups in the database
        List<CodeGroups> codeGroupsList = codeGroupsRepository.findAll();
        assertThat(codeGroupsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCodeGroups() throws Exception {
        // Initialize the database
        codeGroupsRepository.saveAndFlush(codeGroups);

        // Get all the codeGroupsList
        restCodeGroupsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codeGroups.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].json").value(hasItem(DEFAULT_JSON)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)));
    }

    @Test
    @Transactional
    void getCodeGroups() throws Exception {
        // Initialize the database
        codeGroupsRepository.saveAndFlush(codeGroups);

        // Get the codeGroups
        restCodeGroupsMockMvc
            .perform(get(ENTITY_API_URL_ID, codeGroups.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(codeGroups.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.json").value(DEFAULT_JSON))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY));
    }

    @Test
    @Transactional
    void getNonExistingCodeGroups() throws Exception {
        // Get the codeGroups
        restCodeGroupsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCodeGroups() throws Exception {
        // Initialize the database
        codeGroupsRepository.saveAndFlush(codeGroups);

        int databaseSizeBeforeUpdate = codeGroupsRepository.findAll().size();

        // Update the codeGroups
        CodeGroups updatedCodeGroups = codeGroupsRepository.findById(codeGroups.getId()).get();
        // Disconnect from session so that the updates on updatedCodeGroups are not directly saved in db
        em.detach(updatedCodeGroups);
        updatedCodeGroups
            .code(UPDATED_CODE)
            .value(UPDATED_VALUE)
            .description(UPDATED_DESCRIPTION)
            .json(UPDATED_JSON)
            .priority(UPDATED_PRIORITY);
        CodeGroupsDTO codeGroupsDTO = codeGroupsMapper.toDto(updatedCodeGroups);

        restCodeGroupsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codeGroupsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(codeGroupsDTO))
            )
            .andExpect(status().isOk());

        // Validate the CodeGroups in the database
        List<CodeGroups> codeGroupsList = codeGroupsRepository.findAll();
        assertThat(codeGroupsList).hasSize(databaseSizeBeforeUpdate);
        CodeGroups testCodeGroups = codeGroupsList.get(codeGroupsList.size() - 1);
        assertThat(testCodeGroups.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCodeGroups.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testCodeGroups.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCodeGroups.getJson()).isEqualTo(UPDATED_JSON);
        assertThat(testCodeGroups.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void putNonExistingCodeGroups() throws Exception {
        int databaseSizeBeforeUpdate = codeGroupsRepository.findAll().size();
        codeGroups.setId(count.incrementAndGet());

        // Create the CodeGroups
        CodeGroupsDTO codeGroupsDTO = codeGroupsMapper.toDto(codeGroups);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodeGroupsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codeGroupsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(codeGroupsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeGroups in the database
        List<CodeGroups> codeGroupsList = codeGroupsRepository.findAll();
        assertThat(codeGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCodeGroups() throws Exception {
        int databaseSizeBeforeUpdate = codeGroupsRepository.findAll().size();
        codeGroups.setId(count.incrementAndGet());

        // Create the CodeGroups
        CodeGroupsDTO codeGroupsDTO = codeGroupsMapper.toDto(codeGroups);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeGroupsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(codeGroupsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeGroups in the database
        List<CodeGroups> codeGroupsList = codeGroupsRepository.findAll();
        assertThat(codeGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCodeGroups() throws Exception {
        int databaseSizeBeforeUpdate = codeGroupsRepository.findAll().size();
        codeGroups.setId(count.incrementAndGet());

        // Create the CodeGroups
        CodeGroupsDTO codeGroupsDTO = codeGroupsMapper.toDto(codeGroups);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeGroupsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeGroupsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodeGroups in the database
        List<CodeGroups> codeGroupsList = codeGroupsRepository.findAll();
        assertThat(codeGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCodeGroupsWithPatch() throws Exception {
        // Initialize the database
        codeGroupsRepository.saveAndFlush(codeGroups);

        int databaseSizeBeforeUpdate = codeGroupsRepository.findAll().size();

        // Update the codeGroups using partial update
        CodeGroups partialUpdatedCodeGroups = new CodeGroups();
        partialUpdatedCodeGroups.setId(codeGroups.getId());

        partialUpdatedCodeGroups.description(UPDATED_DESCRIPTION).json(UPDATED_JSON).priority(UPDATED_PRIORITY);

        restCodeGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodeGroups.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCodeGroups))
            )
            .andExpect(status().isOk());

        // Validate the CodeGroups in the database
        List<CodeGroups> codeGroupsList = codeGroupsRepository.findAll();
        assertThat(codeGroupsList).hasSize(databaseSizeBeforeUpdate);
        CodeGroups testCodeGroups = codeGroupsList.get(codeGroupsList.size() - 1);
        assertThat(testCodeGroups.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCodeGroups.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testCodeGroups.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCodeGroups.getJson()).isEqualTo(UPDATED_JSON);
        assertThat(testCodeGroups.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void fullUpdateCodeGroupsWithPatch() throws Exception {
        // Initialize the database
        codeGroupsRepository.saveAndFlush(codeGroups);

        int databaseSizeBeforeUpdate = codeGroupsRepository.findAll().size();

        // Update the codeGroups using partial update
        CodeGroups partialUpdatedCodeGroups = new CodeGroups();
        partialUpdatedCodeGroups.setId(codeGroups.getId());

        partialUpdatedCodeGroups
            .code(UPDATED_CODE)
            .value(UPDATED_VALUE)
            .description(UPDATED_DESCRIPTION)
            .json(UPDATED_JSON)
            .priority(UPDATED_PRIORITY);

        restCodeGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodeGroups.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCodeGroups))
            )
            .andExpect(status().isOk());

        // Validate the CodeGroups in the database
        List<CodeGroups> codeGroupsList = codeGroupsRepository.findAll();
        assertThat(codeGroupsList).hasSize(databaseSizeBeforeUpdate);
        CodeGroups testCodeGroups = codeGroupsList.get(codeGroupsList.size() - 1);
        assertThat(testCodeGroups.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCodeGroups.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testCodeGroups.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCodeGroups.getJson()).isEqualTo(UPDATED_JSON);
        assertThat(testCodeGroups.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void patchNonExistingCodeGroups() throws Exception {
        int databaseSizeBeforeUpdate = codeGroupsRepository.findAll().size();
        codeGroups.setId(count.incrementAndGet());

        // Create the CodeGroups
        CodeGroupsDTO codeGroupsDTO = codeGroupsMapper.toDto(codeGroups);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodeGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, codeGroupsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(codeGroupsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeGroups in the database
        List<CodeGroups> codeGroupsList = codeGroupsRepository.findAll();
        assertThat(codeGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCodeGroups() throws Exception {
        int databaseSizeBeforeUpdate = codeGroupsRepository.findAll().size();
        codeGroups.setId(count.incrementAndGet());

        // Create the CodeGroups
        CodeGroupsDTO codeGroupsDTO = codeGroupsMapper.toDto(codeGroups);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(codeGroupsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeGroups in the database
        List<CodeGroups> codeGroupsList = codeGroupsRepository.findAll();
        assertThat(codeGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCodeGroups() throws Exception {
        int databaseSizeBeforeUpdate = codeGroupsRepository.findAll().size();
        codeGroups.setId(count.incrementAndGet());

        // Create the CodeGroups
        CodeGroupsDTO codeGroupsDTO = codeGroupsMapper.toDto(codeGroups);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(codeGroupsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodeGroups in the database
        List<CodeGroups> codeGroupsList = codeGroupsRepository.findAll();
        assertThat(codeGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCodeGroups() throws Exception {
        // Initialize the database
        codeGroupsRepository.saveAndFlush(codeGroups);

        int databaseSizeBeforeDelete = codeGroupsRepository.findAll().size();

        // Delete the codeGroups
        restCodeGroupsMockMvc
            .perform(delete(ENTITY_API_URL_ID, codeGroups.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CodeGroups> codeGroupsList = codeGroupsRepository.findAll();
        assertThat(codeGroupsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
