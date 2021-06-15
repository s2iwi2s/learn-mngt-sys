package com.s2i.lms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.s2i.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageStoreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageStore.class);
        ImageStore imageStore1 = new ImageStore();
        imageStore1.setId(1L);
        ImageStore imageStore2 = new ImageStore();
        imageStore2.setId(imageStore1.getId());
        assertThat(imageStore1).isEqualTo(imageStore2);
        imageStore2.setId(2L);
        assertThat(imageStore1).isNotEqualTo(imageStore2);
        imageStore1.setId(null);
        assertThat(imageStore1).isNotEqualTo(imageStore2);
    }
}
