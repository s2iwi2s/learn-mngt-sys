package com.s2i.lms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageStoreMapperTest {

    private ImageStoreMapper imageStoreMapper;

    @BeforeEach
    public void setUp() {
        imageStoreMapper = new ImageStoreMapperImpl();
    }
}
