package com.s2i.lms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.s2i.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CodeGroupsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeGroupsDTO.class);
        CodeGroupsDTO codeGroupsDTO1 = new CodeGroupsDTO();
        codeGroupsDTO1.setId(1L);
        CodeGroupsDTO codeGroupsDTO2 = new CodeGroupsDTO();
        assertThat(codeGroupsDTO1).isNotEqualTo(codeGroupsDTO2);
        codeGroupsDTO2.setId(codeGroupsDTO1.getId());
        assertThat(codeGroupsDTO1).isEqualTo(codeGroupsDTO2);
        codeGroupsDTO2.setId(2L);
        assertThat(codeGroupsDTO1).isNotEqualTo(codeGroupsDTO2);
        codeGroupsDTO1.setId(null);
        assertThat(codeGroupsDTO1).isNotEqualTo(codeGroupsDTO2);
    }
}
