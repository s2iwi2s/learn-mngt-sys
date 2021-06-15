package com.s2i.lms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.s2i.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageStoreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageStoreDTO.class);
        ImageStoreDTO imageStoreDTO1 = new ImageStoreDTO();
        imageStoreDTO1.setId(1L);
        ImageStoreDTO imageStoreDTO2 = new ImageStoreDTO();
        assertThat(imageStoreDTO1).isNotEqualTo(imageStoreDTO2);
        imageStoreDTO2.setId(imageStoreDTO1.getId());
        assertThat(imageStoreDTO1).isEqualTo(imageStoreDTO2);
        imageStoreDTO2.setId(2L);
        assertThat(imageStoreDTO1).isNotEqualTo(imageStoreDTO2);
        imageStoreDTO1.setId(null);
        assertThat(imageStoreDTO1).isNotEqualTo(imageStoreDTO2);
    }
}
