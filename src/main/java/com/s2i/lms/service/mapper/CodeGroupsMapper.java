package com.s2i.lms.service.mapper;

import com.s2i.lms.domain.*;
import com.s2i.lms.service.dto.CodeGroupsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CodeGroups} and its DTO {@link CodeGroupsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CodeGroupsMapper extends EntityMapper<CodeGroupsDTO, CodeGroups> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CodeGroupsDTO toDtoId(CodeGroups codeGroups);
}
