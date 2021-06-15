package com.s2i.lms.service.mapper;

import com.s2i.lms.domain.*;
import com.s2i.lms.service.dto.ImageStoreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ImageStore} and its DTO {@link ImageStoreDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ImageStoreMapper extends EntityMapper<ImageStoreDTO, ImageStore> {}
