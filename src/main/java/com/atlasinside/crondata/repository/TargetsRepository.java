package com.atlasinside.crondata.repository;

import com.atlasinside.crondata.domain.Targets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Targets entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TargetsRepository extends JpaRepository<Targets, Long>, JpaSpecificationExecutor<Targets> {

}
