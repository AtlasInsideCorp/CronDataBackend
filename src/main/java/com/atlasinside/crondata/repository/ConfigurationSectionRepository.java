package com.atlasinside.crondata.repository;

import com.atlasinside.crondata.domain.ConfigurationSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ConfigurationSection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigurationSectionRepository extends JpaRepository<ConfigurationSection, Long>, JpaSpecificationExecutor<ConfigurationSection> {
}
