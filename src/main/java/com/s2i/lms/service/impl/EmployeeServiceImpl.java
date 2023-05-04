package com.s2i.lms.service.impl;

import com.s2i.lms.domain.Employee;
import com.s2i.lms.service.EmployeeService;
import com.s2i.lms.service.dto.EmployeeResponseDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final RestTemplate restTemplate;
    private final String serviceUrl;

    public EmployeeServiceImpl(RestTemplateBuilder builder) {
        this.restTemplate = builder
            .setConnectTimeout(Duration.ofMillis(60000))
            .setReadTimeout(Duration.ofMillis(60000))
            .build();
        this.serviceUrl = "https://dummy.restapiexample.com/api/v1";
    }

    public List<Employee> getAllEmployees() throws Exception {
        ResponseEntity<EmployeeResponseDTO> response = restTemplate.getForEntity(serviceUrl + "/employees", EmployeeResponseDTO.class);
        return Objects.requireNonNull(response.getBody()).getData();
    }

    public List<Employee> getAllEmployeesWithAgeEqual(int age) throws Exception {
        return getAllEmployees().stream().filter(e -> e.getEmployeeAge() == age).collect(Collectors.toList());
    }

    public List<Employee> getAllEmployeesWithAgeGreaterThan(Integer age) throws Exception {
        return getAllEmployees().stream().filter(e -> e.getEmployeeAge() > age).collect(Collectors.toList());
    }

    public List<Employee> getAllEmployeesWithAgeLessThan(Integer age) throws Exception {
        return getAllEmployees().stream().filter(e -> e.getEmployeeAge() < age).collect(Collectors.toList());
    }
}
