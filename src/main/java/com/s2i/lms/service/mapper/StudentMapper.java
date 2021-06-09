package com.s2i.lms.service.mapper;

import com.s2i.lms.domain.*;
import com.s2i.lms.service.dto.StudentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Student} and its DTO {@link StudentDTO}.
 */
@Mapper(componentModel = "spring", uses = { CodeGroupsMapper.class })
public interface StudentMapper extends EntityMapper<StudentDTO, Student> {
    @Mapping(target = "gender", source = "gender", qualifiedByName = "id")
    @Mapping(target = "parentCivilStatus", source = "parentCivilStatus", qualifiedByName = "id")
    @Mapping(target = "course", source = "course", qualifiedByName = "id")
    StudentDTO toDto(Student s);
}
