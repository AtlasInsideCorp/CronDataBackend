package com.ps.cromdata.repository;

import com.ps.cromdata.domain.TargetInstances;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the TargetInstances entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TargetInstancesRepository extends JpaRepository<TargetInstances, Long> {
}
