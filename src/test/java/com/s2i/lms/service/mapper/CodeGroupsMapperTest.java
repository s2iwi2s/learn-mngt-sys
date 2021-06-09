package com.s2i.lms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CodeGroupsMapperTest {

    private CodeGroupsMapper codeGroupsMapper;

    @BeforeEach
    public void setUp() {
        codeGroupsMapper = new CodeGroupsMapperImpl();
    }
}
