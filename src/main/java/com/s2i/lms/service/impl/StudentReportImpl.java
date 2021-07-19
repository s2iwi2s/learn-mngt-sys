package com.s2i.lms.service.impl;

import com.s2i.lms.domain.Student;
import com.s2i.lms.repository.StudentRepository;
import com.s2i.lms.service.AbstractReport;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

public class StudentReportImpl extends AbstractReport<Student> {
    final StudentRepository studentRepository;

    public StudentReportImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;

        this.createReport();
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
    public String[] getHeader() {
        return new String[] {
            "ID",
            "LRN",
            "Birth Place",
            "Birth Date"
        };
    }

    @Override
    public void createRow(Student student, Row row) {
    	int i = 0;
        setCell(student.getId(), i++, row);
        setCell(student.getLrn(), i++, row);
        setCell(student.getBirthPlace(), i++, row);
        setCell(student.getBirthDate(), i, row);
    }

    public void createFooter() {
        Row row = sh.createRow(list.size() + 1);
        // double totalAmount = list.stream().mapToDouble(item -> item.getAmount().doubleValue()).sum();
        this.setCell(list.size(), 1, row);
    }
}
