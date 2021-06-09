package com.s2i.lms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.s2i.lms.IntegrationTest;
import com.s2i.lms.domain.Student;
import com.s2i.lms.repository.StudentRepository;
import com.s2i.lms.service.dto.StudentDTO;
import com.s2i.lms.service.mapper.StudentMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link StudentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentResourceIT {

    private static final String DEFAULT_LRN = "AAAAAAAAAA";
    private static final String UPDATED_LRN = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_BIRTH_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_BIRTH_PLACE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NO = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_FATHERS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FATHERS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FATHERS_OCCUPATION = "AAAAAAAAAA";
    private static final String UPDATED_FATHERS_OCCUPATION = "BBBBBBBBBB";

    private static final String DEFAULT_MOTHERS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MOTHERS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MOTHERS_OCCUPATION = "AAAAAAAAAA";
    private static final String UPDATED_MOTHERS_OCCUPATION = "BBBBBBBBBB";

    private static final String DEFAULT_GUARDIAN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/students";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentMockMvc;

    private Student student;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createEntity(EntityManager em) {
        Student student = new Student()
            .lrn(DEFAULT_LRN)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .birthPlace(DEFAULT_BIRTH_PLACE)
            .contactNo(DEFAULT_CONTACT_NO)
            .address1(DEFAULT_ADDRESS_1)
            .address2(DEFAULT_ADDRESS_2)
            .city(DEFAULT_CITY)
            .zipCode(DEFAULT_ZIP_CODE)
            .country(DEFAULT_COUNTRY)
            .fathersName(DEFAULT_FATHERS_NAME)
            .fathersOccupation(DEFAULT_FATHERS_OCCUPATION)
            .mothersName(DEFAULT_MOTHERS_NAME)
            .mothersOccupation(DEFAULT_MOTHERS_OCCUPATION)
            .guardianName(DEFAULT_GUARDIAN_NAME);
        return student;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createUpdatedEntity(EntityManager em) {
        Student student = new Student()
            .lrn(UPDATED_LRN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .contactNo(UPDATED_CONTACT_NO)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .city(UPDATED_CITY)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .fathersName(UPDATED_FATHERS_NAME)
            .fathersOccupation(UPDATED_FATHERS_OCCUPATION)
            .mothersName(UPDATED_MOTHERS_NAME)
            .mothersOccupation(UPDATED_MOTHERS_OCCUPATION)
            .guardianName(UPDATED_GUARDIAN_NAME);
        return student;
    }

    @BeforeEach
    public void initTest() {
        student = createEntity(em);
    }

    @Test
    @Transactional
    void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();
        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);
        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isCreated());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getLrn()).isEqualTo(DEFAULT_LRN);
        assertThat(testStudent.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testStudent.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testStudent.getBirthPlace()).isEqualTo(DEFAULT_BIRTH_PLACE);
        assertThat(testStudent.getContactNo()).isEqualTo(DEFAULT_CONTACT_NO);
        assertThat(testStudent.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testStudent.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testStudent.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testStudent.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testStudent.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testStudent.getFathersName()).isEqualTo(DEFAULT_FATHERS_NAME);
        assertThat(testStudent.getFathersOccupation()).isEqualTo(DEFAULT_FATHERS_OCCUPATION);
        assertThat(testStudent.getMothersName()).isEqualTo(DEFAULT_MOTHERS_NAME);
        assertThat(testStudent.getMothersOccupation()).isEqualTo(DEFAULT_MOTHERS_OCCUPATION);
        assertThat(testStudent.getGuardianName()).isEqualTo(DEFAULT_GUARDIAN_NAME);
    }

    @Test
    @Transactional
    void createStudentWithExistingId() throws Exception {
        // Create the Student with an existing ID
        student.setId(1L);
        StudentDTO studentDTO = studentMapper.toDto(student);

        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStudents() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].lrn").value(hasItem(DEFAULT_LRN)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE)))
            .andExpect(jsonPath("$.[*].contactNo").value(hasItem(DEFAULT_CONTACT_NO)))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1)))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].fathersName").value(hasItem(DEFAULT_FATHERS_NAME)))
            .andExpect(jsonPath("$.[*].fathersOccupation").value(hasItem(DEFAULT_FATHERS_OCCUPATION)))
            .andExpect(jsonPath("$.[*].mothersName").value(hasItem(DEFAULT_MOTHERS_NAME)))
            .andExpect(jsonPath("$.[*].mothersOccupation").value(hasItem(DEFAULT_MOTHERS_OCCUPATION)))
            .andExpect(jsonPath("$.[*].guardianName").value(hasItem(DEFAULT_GUARDIAN_NAME)));
    }

    @Test
    @Transactional
    void getStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get the student
        restStudentMockMvc
            .perform(get(ENTITY_API_URL_ID, student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(student.getId().intValue()))
            .andExpect(jsonPath("$.lrn").value(DEFAULT_LRN))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.birthPlace").value(DEFAULT_BIRTH_PLACE))
            .andExpect(jsonPath("$.contactNo").value(DEFAULT_CONTACT_NO))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.fathersName").value(DEFAULT_FATHERS_NAME))
            .andExpect(jsonPath("$.fathersOccupation").value(DEFAULT_FATHERS_OCCUPATION))
            .andExpect(jsonPath("$.mothersName").value(DEFAULT_MOTHERS_NAME))
            .andExpect(jsonPath("$.mothersOccupation").value(DEFAULT_MOTHERS_OCCUPATION))
            .andExpect(jsonPath("$.guardianName").value(DEFAULT_GUARDIAN_NAME));
    }

    @Test
    @Transactional
    void getNonExistingStudent() throws Exception {
        // Get the student
        restStudentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student
        Student updatedStudent = studentRepository.findById(student.getId()).get();
        // Disconnect from session so that the updates on updatedStudent are not directly saved in db
        em.detach(updatedStudent);
        updatedStudent
            .lrn(UPDATED_LRN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .contactNo(UPDATED_CONTACT_NO)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .city(UPDATED_CITY)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .fathersName(UPDATED_FATHERS_NAME)
            .fathersOccupation(UPDATED_FATHERS_OCCUPATION)
            .mothersName(UPDATED_MOTHERS_NAME)
            .mothersOccupation(UPDATED_MOTHERS_OCCUPATION)
            .guardianName(UPDATED_GUARDIAN_NAME);
        StudentDTO studentDTO = studentMapper.toDto(updatedStudent);

        restStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getLrn()).isEqualTo(UPDATED_LRN);
        assertThat(testStudent.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testStudent.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testStudent.getBirthPlace()).isEqualTo(UPDATED_BIRTH_PLACE);
        assertThat(testStudent.getContactNo()).isEqualTo(UPDATED_CONTACT_NO);
        assertThat(testStudent.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testStudent.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testStudent.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testStudent.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testStudent.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testStudent.getFathersName()).isEqualTo(UPDATED_FATHERS_NAME);
        assertThat(testStudent.getFathersOccupation()).isEqualTo(UPDATED_FATHERS_OCCUPATION);
        assertThat(testStudent.getMothersName()).isEqualTo(UPDATED_MOTHERS_NAME);
        assertThat(testStudent.getMothersOccupation()).isEqualTo(UPDATED_MOTHERS_OCCUPATION);
        assertThat(testStudent.getGuardianName()).isEqualTo(UPDATED_GUARDIAN_NAME);
    }

    @Test
    @Transactional
    void putNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentWithPatch() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student using partial update
        Student partialUpdatedStudent = new Student();
        partialUpdatedStudent.setId(student.getId());

        partialUpdatedStudent
            .lrn(UPDATED_LRN)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .city(UPDATED_CITY)
            .fathersName(UPDATED_FATHERS_NAME)
            .fathersOccupation(UPDATED_FATHERS_OCCUPATION)
            .mothersName(UPDATED_MOTHERS_NAME);

        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudent))
            )
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getLrn()).isEqualTo(UPDATED_LRN);
        assertThat(testStudent.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testStudent.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testStudent.getBirthPlace()).isEqualTo(UPDATED_BIRTH_PLACE);
        assertThat(testStudent.getContactNo()).isEqualTo(DEFAULT_CONTACT_NO);
        assertThat(testStudent.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testStudent.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testStudent.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testStudent.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testStudent.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testStudent.getFathersName()).isEqualTo(UPDATED_FATHERS_NAME);
        assertThat(testStudent.getFathersOccupation()).isEqualTo(UPDATED_FATHERS_OCCUPATION);
        assertThat(testStudent.getMothersName()).isEqualTo(UPDATED_MOTHERS_NAME);
        assertThat(testStudent.getMothersOccupation()).isEqualTo(DEFAULT_MOTHERS_OCCUPATION);
        assertThat(testStudent.getGuardianName()).isEqualTo(DEFAULT_GUARDIAN_NAME);
    }

    @Test
    @Transactional
    void fullUpdateStudentWithPatch() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student using partial update
        Student partialUpdatedStudent = new Student();
        partialUpdatedStudent.setId(student.getId());

        partialUpdatedStudent
            .lrn(UPDATED_LRN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .contactNo(UPDATED_CONTACT_NO)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .city(UPDATED_CITY)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .fathersName(UPDATED_FATHERS_NAME)
            .fathersOccupation(UPDATED_FATHERS_OCCUPATION)
            .mothersName(UPDATED_MOTHERS_NAME)
            .mothersOccupation(UPDATED_MOTHERS_OCCUPATION)
            .guardianName(UPDATED_GUARDIAN_NAME);

        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudent))
            )
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getLrn()).isEqualTo(UPDATED_LRN);
        assertThat(testStudent.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testStudent.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testStudent.getBirthPlace()).isEqualTo(UPDATED_BIRTH_PLACE);
        assertThat(testStudent.getContactNo()).isEqualTo(UPDATED_CONTACT_NO);
        assertThat(testStudent.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testStudent.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testStudent.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testStudent.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testStudent.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testStudent.getFathersName()).isEqualTo(UPDATED_FATHERS_NAME);
        assertThat(testStudent.getFathersOccupation()).isEqualTo(UPDATED_FATHERS_OCCUPATION);
        assertThat(testStudent.getMothersName()).isEqualTo(UPDATED_MOTHERS_NAME);
        assertThat(testStudent.getMothersOccupation()).isEqualTo(UPDATED_MOTHERS_OCCUPATION);
        assertThat(testStudent.getGuardianName()).isEqualTo(UPDATED_GUARDIAN_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(studentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeDelete = studentRepository.findAll().size();

        // Delete the student
        restStudentMockMvc
            .perform(delete(ENTITY_API_URL_ID, student.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
