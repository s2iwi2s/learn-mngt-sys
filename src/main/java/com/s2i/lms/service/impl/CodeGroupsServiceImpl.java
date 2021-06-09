package com.s2i.lms.service.impl;

import com.s2i.lms.domain.CodeGroups;
import com.s2i.lms.repository.CodeGroupsRepository;
import com.s2i.lms.service.CodeGroupsService;
import com.s2i.lms.service.dto.CodeGroupsDTO;
import com.s2i.lms.service.mapper.CodeGroupsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CodeGroups}.
 */
@Service
@Transactional
public class CodeGroupsServiceImpl implements CodeGroupsService {

    private final Logger log = LoggerFactory.getLogger(CodeGroupsServiceImpl.class);

    private final CodeGroupsRepository codeGroupsRepository;

    private final CodeGroupsMapper codeGroupsMapper;

    public CodeGroupsServiceImpl(CodeGroupsRepository codeGroupsRepository, CodeGroupsMapper codeGroupsMapper) {
        this.codeGroupsRepository = codeGroupsRepository;
        this.codeGroupsMapper = codeGroupsMapper;
    }

    @Override
    public CodeGroupsDTO save(CodeGroupsDTO codeGroupsDTO) {
        log.debug("Request to save CodeGroups : {}", codeGroupsDTO);
        CodeGroups codeGroups = codeGroupsMapper.toEntity(codeGroupsDTO);
        codeGroups = codeGroupsRepository.save(codeGroups);
        return codeGroupsMapper.toDto(codeGroups);
    }

    @Override
    public Optional<CodeGroupsDTO> partialUpdate(CodeGroupsDTO codeGroupsDTO) {
        log.debug("Request to partially update CodeGroups : {}", codeGroupsDTO);

        return codeGroupsRepository
            .findById(codeGroupsDTO.getId())
            .map(
                existingCodeGroups -> {
                    codeGroupsMapper.partialUpdate(existingCodeGroups, codeGroupsDTO);
                    return existingCodeGroups;
                }
            )
            .map(codeGroupsRepository::save)
            .map(codeGroupsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CodeGroupsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CodeGroups");
        return codeGroupsRepository.findAll(pageable).map(codeGroupsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CodeGroupsDTO> findOne(Long id) {
        log.debug("Request to get CodeGroups : {}", id);
        return codeGroupsRepository.findById(id).map(codeGroupsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CodeGroups : {}", id);
        codeGroupsRepository.deleteById(id);
    }
}
