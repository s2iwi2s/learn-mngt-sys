package com.s2i.lms.repository;

import com.s2i.lms.domain.ImageStore;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ImageStore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageStoreRepository extends JpaRepository<ImageStore, Long> {}
