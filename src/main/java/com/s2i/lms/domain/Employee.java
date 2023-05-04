package com.s2i.lms.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Employee {

    private int id;

    @JsonAlias("employee_name")
    private String employeeName;

    @JsonAlias("employee_salary")
    private double employeeSalary;

    @JsonAlias("employee_age")
    private int employeeAge;

    @JsonAlias("employee_image")
    private String employeeImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public double getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(double employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    public int getEmployeeAge() {
        return employeeAge;
    }

    public void setEmployeeAge(int employeeAge) {
        this.employeeAge = employeeAge;
    }

    public String getEmployeeImage() {
        return employeeImage;
    }

    public void setEmployeeImage(String employeeImage) {
        this.employeeImage = employeeImage;
    }
}
