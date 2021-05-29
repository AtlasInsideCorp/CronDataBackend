package com.ps.cromdata.repository;

import com.ps.cromdata.domain.Alerts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Alerts entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlertsRepository extends JpaRepository<Alerts, Long>, JpaSpecificationExecutor<Alerts> {
}
