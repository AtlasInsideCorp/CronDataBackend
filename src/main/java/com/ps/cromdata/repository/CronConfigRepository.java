package com.ps.cromdata.repository;

import com.ps.cromdata.domain.CronConfig;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CronConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CronConfigRepository extends JpaRepository<CronConfig, Long> {
}
