package com.s2i.lms.repository;

import com.s2i.lms.domain.CodeGroups;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CodeGroups entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodeGroupsRepository extends JpaRepository<CodeGroups, Long> {}
