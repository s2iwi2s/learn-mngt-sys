package com.s2i.lms.service.impl;

import com.s2i.lms.domain.Student;
import com.s2i.lms.repository.StudentRepository;
import com.s2i.lms.service.AbstractReport;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

public class StudentReportImpl extends AbstractReport<Student> {
    final StudentRepository studentRepository;

    public StudentReportImpl(StudentRepository studentRepository) throws Exception {
        super();
        this.studentRepository = studentRepository;

        this.createReport();
    }

    @Override
    public String getTemplateName() {
        return "Student-Report";
    }

    @Override
    public List<Student> getList() {
        return studentRepository.findAll();
    }

    @Override
    public String getFileName() {
        return "student_list";
    }

    @Override
    public int getHeaderLength(){
        return 4;
    }

    @Override
    public void createRow(Student student, Row row) {
    	int i = 0;
        setCellt(student.getId(), i++, row);
        setCellt(student.getLrn(), i++, row);
        setCellt(student.getBirthPlace(), i++, row);
        setCellt(student.getBirthDate(), i, row);
    }

    public void createFooter() {
        Row row = sh.createRow(list.size() + 1);
        // double totalAmount = list.stream().mapToDouble(item -> item.getAmount().doubleValue()).sum();
        this.setCellt(list.size(), 1, row);
    }
}
