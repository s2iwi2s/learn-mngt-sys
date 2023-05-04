package com.s2i.lms.service;

import com.s2i.lms.domain.Employee;

import java.util.List;

public interface EmployeeService {
    public List<Employee> getAllEmployees() throws Exception;

    public List<Employee> getAllEmployeesWithAgeEqual(int age) throws Exception;

    public List<Employee> getAllEmployeesWithAgeGreaterThan(Integer age) throws Exception;

    public List<Employee> getAllEmployeesWithAgeLessThan(Integer age) throws Exception;
}
