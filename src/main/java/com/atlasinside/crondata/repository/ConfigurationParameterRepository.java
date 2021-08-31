package com.atlasinside.crondata.repository;

import com.atlasinside.crondata.domain.ConfigurationParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UtmConfigurationParameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigurationParameterRepository extends JpaRepository<ConfigurationParameter, Long>, JpaSpecificationExecutor<ConfigurationParameter> {
}
