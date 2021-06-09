package com.s2i.lms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.s2i.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CodeGroupsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeGroups.class);
        CodeGroups codeGroups1 = new CodeGroups();
        codeGroups1.setId(1L);
        CodeGroups codeGroups2 = new CodeGroups();
        codeGroups2.setId(codeGroups1.getId());
        assertThat(codeGroups1).isEqualTo(codeGroups2);
        codeGroups2.setId(2L);
        assertThat(codeGroups1).isNotEqualTo(codeGroups2);
        codeGroups1.setId(null);
        assertThat(codeGroups1).isNotEqualTo(codeGroups2);
    }
}
