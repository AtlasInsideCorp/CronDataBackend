package com.atlasinside.crondata.repository;

import com.atlasinside.crondata.domain.AppCategory;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AppCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppCategoryRepository extends JpaRepository<AppCategory, Long> {
}
