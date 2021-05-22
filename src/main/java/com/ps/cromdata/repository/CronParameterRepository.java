package com.ps.cromdata.repository;

import com.ps.cromdata.domain.CronParameter;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CronParameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CronParameterRepository extends JpaRepository<CronParameter, Long> {
}
