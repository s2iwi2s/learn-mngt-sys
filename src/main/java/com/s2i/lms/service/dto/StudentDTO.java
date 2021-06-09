package com.s2i.lms.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.s2i.lms.domain.Student} entity.
 */
public class StudentDTO implements Serializable {

    private Long id;

    private String lrn;

    private String firstName;

    private String lastName;

    private Instant birthDate;

    private String birthPlace;

    private String contactNo;

    private String address1;

    private String address2;

    private String city;

    private String zipCode;

    private String country;

    private String fathersName;

    private String fathersOccupation;

    private String mothersName;

    private String mothersOccupation;

    private String guardianName;

    private CodeGroupsDTO gender;

    private CodeGroupsDTO parentCivilStatus;

    private CodeGroupsDTO course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLrn() {
        return lrn;
    }

    public void setLrn(String lrn) {
        this.lrn = lrn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getFathersOccupation() {
        return fathersOccupation;
    }

    public void setFathersOccupation(String fathersOccupation) {
        this.fathersOccupation = fathersOccupation;
    }

    public String getMothersName() {
        return mothersName;
    }

    public void setMothersName(String mothersName) {
        this.mothersName = mothersName;
    }

    public String getMothersOccupation() {
        return mothersOccupation;
    }

    public void setMothersOccupation(String mothersOccupation) {
        this.mothersOccupation = mothersOccupation;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public CodeGroupsDTO getGender() {
        return gender;
    }

    public void setGender(CodeGroupsDTO gender) {
        this.gender = gender;
    }

    public CodeGroupsDTO getParentCivilStatus() {
        return parentCivilStatus;
    }

    public void setParentCivilStatus(CodeGroupsDTO parentCivilStatus) {
        this.parentCivilStatus = parentCivilStatus;
    }

    public CodeGroupsDTO getCourse() {
        return course;
    }

    public void setCourse(CodeGroupsDTO course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentDTO)) {
            return false;
        }

        StudentDTO studentDTO = (StudentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentDTO{" +
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
            ", gender=" + getGender() +
            ", parentCivilStatus=" + getParentCivilStatus() +
            ", course=" + getCourse() +
            "}";
    }
}
