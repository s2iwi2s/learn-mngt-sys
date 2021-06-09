package com.s2i.lms.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "lrn")
    private String lrn;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private Instant birthDate;

    @Column(name = "birth_place")
    private String birthPlace;

    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "address_1")
    private String address1;

    @Column(name = "address_2")
    private String address2;

    @Column(name = "city")
    private String city;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "country")
    private String country;

    @Column(name = "fathers_name")
    private String fathersName;

    @Column(name = "fathers_occupation")
    private String fathersOccupation;

    @Column(name = "mothers_name")
    private String mothersName;

    @Column(name = "mothers_occupation")
    private String mothersOccupation;

    @Column(name = "guardian_name")
    private String guardianName;

    @OneToOne
    @JoinColumn(unique = true)
    private CodeGroups gender;

    @OneToOne
    @JoinColumn(unique = true)
    private CodeGroups parentCivilStatus;

    @OneToOne
    @JoinColumn(unique = true)
    private CodeGroups course;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student id(Long id) {
        this.id = id;
        return this;
    }

    public String getLrn() {
        return this.lrn;
    }

    public Student lrn(String lrn) {
        this.lrn = lrn;
        return this;
    }

    public void setLrn(String lrn) {
        this.lrn = lrn;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Student firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Student lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Instant getBirthDate() {
        return this.birthDate;
    }

    public Student birthDate(Instant birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return this.birthPlace;
    }

    public Student birthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
        return this;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getContactNo() {
        return this.contactNo;
    }

    public Student contactNo(String contactNo) {
        this.contactNo = contactNo;
        return this;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress1() {
        return this.address1;
    }

    public Student address1(String address1) {
        this.address1 = address1;
        return this;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return this.address2;
    }

    public Student address2(String address2) {
        this.address2 = address2;
        return this;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return this.city;
    }

    public Student city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public Student zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return this.country;
    }

    public Student country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFathersName() {
        return this.fathersName;
    }

    public Student fathersName(String fathersName) {
        this.fathersName = fathersName;
        return this;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getFathersOccupation() {
        return this.fathersOccupation;
    }

    public Student fathersOccupation(String fathersOccupation) {
        this.fathersOccupation = fathersOccupation;
        return this;
    }

    public void setFathersOccupation(String fathersOccupation) {
        this.fathersOccupation = fathersOccupation;
    }

    public String getMothersName() {
        return this.mothersName;
    }

    public Student mothersName(String mothersName) {
        this.mothersName = mothersName;
        return this;
    }

    public void setMothersName(String mothersName) {
        this.mothersName = mothersName;
    }

    public String getMothersOccupation() {
        return this.mothersOccupation;
    }

    public Student mothersOccupation(String mothersOccupation) {
        this.mothersOccupation = mothersOccupation;
        return this;
    }

    public void setMothersOccupation(String mothersOccupation) {
        this.mothersOccupation = mothersOccupation;
    }

    public String getGuardianName() {
        return this.guardianName;
    }

    public Student guardianName(String guardianName) {
        this.guardianName = guardianName;
        return this;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public CodeGroups getGender() {
        return this.gender;
    }

    public Student gender(CodeGroups codeGroups) {
        this.setGender(codeGroups);
        return this;
    }

    public void setGender(CodeGroups codeGroups) {
        this.gender = codeGroups;
    }

    public CodeGroups getParentCivilStatus() {
        return this.parentCivilStatus;
    }

    public Student parentCivilStatus(CodeGroups codeGroups) {
        this.setParentCivilStatus(codeGroups);
        return this;
    }

    public void setParentCivilStatus(CodeGroups codeGroups) {
        this.parentCivilStatus = codeGroups;
    }

    public CodeGroups getCourse() {
        return this.course;
    }

    public Student course(CodeGroups codeGroups) {
        this.setCourse(codeGroups);
        return this;
    }

    public void setCourse(CodeGroups codeGroups) {
        this.course = codeGroups;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return id != null && id.equals(((Student) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", lrn='" + getLrn() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", birthPlace='" + getBirthPlace() + "'" +
            ", contactNo='" + getContactNo() + "'" +
            ", address1='" + getAddress1() + "'" +
            ", address2='" + getAddress2() + "'" +
            ", city='" + getCity() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", country='" + getCountry() + "'" +
            ", fathersName='" + getFathersName() + "'" +
            ", fathersOccupation='" + getFathersOccupation() + "'" +
            ", mothersName='" + getMothersName() + "'" +
            ", mothersOccupation='" + getMothersOccupation() + "'" +
            ", guardianName='" + getGuardianName() + "'" +
            "}";
    }
}
