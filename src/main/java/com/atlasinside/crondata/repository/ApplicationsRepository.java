package com.atlasinside.crondata.repository;

import com.atlasinside.crondata.domain.Applications;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Applications entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationsRepository extends JpaRepository<Applications, Long>, JpaSpecificationExecutor<Applications> {
}
