package com.s2i.lms.service.impl;

import com.s2i.lms.domain.ImageStore;
import com.s2i.lms.repository.ImageStoreRepository;
import com.s2i.lms.service.ImageStoreService;
import com.s2i.lms.service.dto.ImageStoreDTO;
import com.s2i.lms.service.mapper.ImageStoreMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ImageStore}.
 */
@Service
@Transactional
public class ImageStoreServiceImpl implements ImageStoreService {

    private final Logger log = LoggerFactory.getLogger(ImageStoreServiceImpl.class);

    private final ImageStoreRepository imageStoreRepository;

    private final ImageStoreMapper imageStoreMapper;

    public ImageStoreServiceImpl(ImageStoreRepository imageStoreRepository, ImageStoreMapper imageStoreMapper) {
        this.imageStoreRepository = imageStoreRepository;
        this.imageStoreMapper = imageStoreMapper;
    }

    @Override
    public ImageStoreDTO save(ImageStoreDTO imageStoreDTO) {
        log.debug("Request to save ImageStore : {}", imageStoreDTO);
        ImageStore imageStore = imageStoreMapper.toEntity(imageStoreDTO);
        imageStore = imageStoreRepository.save(imageStore);
        return imageStoreMapper.toDto(imageStore);
    }

    @Override
    public Optional<ImageStoreDTO> partialUpdate(ImageStoreDTO imageStoreDTO) {
        log.debug("Request to partially update ImageStore : {}", imageStoreDTO);

        return imageStoreRepository
            .findById(imageStoreDTO.getId())
            .map(
                existingImageStore -> {
                    imageStoreMapper.partialUpdate(existingImageStore, imageStoreDTO);
                    return existingImageStore;
                }
            )
            .map(imageStoreRepository::save)
            .map(imageStoreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImageStoreDTO> findAll() {
        log.debug("Request to get all ImageStores");
        return imageStoreRepository.findAll().stream().map(imageStoreMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImageStoreDTO> findOne(Long id) {
        log.debug("Request to get ImageStore : {}", id);
        return imageStoreRepository.findById(id).map(imageStoreMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ImageStore : {}", id);
        imageStoreRepository.deleteById(id);
    }
}
