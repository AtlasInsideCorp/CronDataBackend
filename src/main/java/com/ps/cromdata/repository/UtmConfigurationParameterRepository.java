package com.ps.cromdata.repository;

import com.ps.cromdata.domain.UtmConfigurationParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UtmConfigurationParameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtmConfigurationParameterRepository extends JpaRepository<UtmConfigurationParameter, Long>, JpaSpecificationExecutor<UtmConfigurationParameter> {
}
