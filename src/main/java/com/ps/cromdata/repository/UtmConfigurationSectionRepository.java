package com.ps.cromdata.repository;

import com.ps.cromdata.domain.UtmConfigurationSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UtmConfigurationSection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtmConfigurationSectionRepository extends JpaRepository<UtmConfigurationSection, Long>, JpaSpecificationExecutor<UtmConfigurationSection> {
}
