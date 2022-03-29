package com.atlasinside.crondata.service;

import com.atlasinside.crondata.domain.ConfigurationSection;
import com.atlasinside.crondata.domain.User;
import com.atlasinside.crondata.repository.ConfigurationSectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing ConfigurationSection.
 */
@Service
@Transactional
public class ConfigurationSectionService {

    private static final String CLASSNAME = "ConfigurationSectionService";

    private final Logger log = LoggerFactory.getLogger(ConfigurationSectionService.class);

    private final ConfigurationSectionRepository configurationSectionRepository;
    private final MailService mailService;
    private final UserService userService;

    public ConfigurationSectionService(ConfigurationSectionRepository configurationSectionRepository,
                                       MailService mailService, UserService userService) {
        this.configurationSectionRepository = configurationSectionRepository;
        this.mailService = mailService;
        this.userService = userService;
    }

    /**
     * Save a configurationSection.
     *
     * @param configurationSection the entity to save
     * @return the persisted entity
     */
    public ConfigurationSection save(ConfigurationSection configurationSection) {
        log.debug("Request to save ConfigurationSection : {}", configurationSection);
        return configurationSectionRepository.save(configurationSection);
    }

    /**
     * Get all the configurationSections.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ConfigurationSection> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigurationSections");
        return configurationSectionRepository.findAll(pageable);
    }


    /**
     * Get one configurationSection by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ConfigurationSection> findOne(Long id) {
        log.debug("Request to get ConfigurationSection : {}", id);
        return configurationSectionRepository.findById(id);
    }

    /**
     * Delete the configurationSection by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ConfigurationSection : {}", id);
        configurationSectionRepository.deleteById(id);
    }

    public void checkEmailConfiguration() throws Exception {
        final String ctx = CLASSNAME + ".checkEmailConfiguration";
        try {
            User user = userService.getCurrentUserLogin();
            List<String> to = Collections.singletonList(user.getEmail());
            mailService.sendCheckEmail(to);
        } catch (Exception e) {
            throw new Exception(ctx + ": " + e.getMessage());
        }
    }

}
