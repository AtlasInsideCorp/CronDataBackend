package com.ps.cromdata.repository;

import com.ps.cromdata.domain.Applications;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Applications entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationsRepository extends JpaRepository<Applications, Long>, JpaSpecificationExecutor<Applications> {
}
