package com.ps.cromdata.repository;

import com.ps.cromdata.domain.Targets;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Targets entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TargetsRepository extends JpaRepository<Targets, Long>, JpaSpecificationExecutor<Targets> {
}
