package com.atlasinside.crondata.service;

import com.atlasinside.crondata.util.CipherUtil;
import com.atlasinside.crondata.config.Constants;
import com.atlasinside.crondata.repository.ConfigurationParameterRepository;
import com.atlasinside.crondata.domain.ConfigurationParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing ConfigurationParameter.
 */
@Service
@Transactional
public class ConfigurationParameterService {

    private static final String CLASSNAME = "ConfigurationParameterService";
    private final Logger log = LoggerFactory.getLogger(ConfigurationParameterService.class);

    private final ConfigurationParameterRepository configurationParameterRepository;
    private final ApplicationPropertyService applicationPropertyService;
    private final UserService userService;

    public ConfigurationParameterService(ConfigurationParameterRepository configurationParameterRepository,
                                         ApplicationPropertyService applicationPropertyService,
                                         UserService userService) {
        this.configurationParameterRepository = configurationParameterRepository;
        this.applicationPropertyService = applicationPropertyService;
        this.userService = userService;
    }

    /**
     * Save a configurationParameter.
     *
     * @param parameter the entity to save
     * @return the persisted entity
     */
    public ConfigurationParameter save(@Valid ConfigurationParameter parameter) {
        final String ctx = CLASSNAME + ".";

        try {
            applicationPropertyService.updatePropertySource(parameter.getConfParamShort(), parameter.getConfParamValue());

            if (parameter.getConfParamDatatype().equalsIgnoreCase("password"))
                parameter.setConfParamValue(CipherUtil.encrypt(parameter.getConfParamValue(), Constants.CONF_PARAM_PWD_TYPE_SECRET));

            return configurationParameterRepository.save(parameter);
        } catch (Exception e) {
            throw new RuntimeException(ctx + ": " + e.getMessage());
        }
    }

    /**
     * @param parameters
     */
    public void saveAll(List<ConfigurationParameter> parameters) throws Exception {
        final String ctx = CLASSNAME + ".saveAll";

        try {
            for (ConfigurationParameter parameter : parameters) {
                applicationPropertyService.updatePropertySource(parameter.getConfParamShort(),
                    parameter.getConfParamValue());

                if (parameter.getConfParamDatatype().equalsIgnoreCase("password"))
                    parameter.setConfParamValue(CipherUtil.encrypt(parameter.getConfParamValue(), Constants.CONF_PARAM_PWD_TYPE_SECRET));
            }

            configurationParameterRepository.saveAll(parameters);

        } catch (Exception e) {
            throw new RuntimeException(ctx + ": " + e.getMessage());
        }
    }

    /**
     * Get all the configurationParameters.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ConfigurationParameter> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigurationParameters");
        return configurationParameterRepository.findAll(pageable);
    }


    /**
     * Get one configurationParameter by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ConfigurationParameter> findOne(Long id) {
        log.debug("Request to get ConfigurationParameter : {}", id);
        return configurationParameterRepository.findById(id);
    }

    /**
     * Delete the configurationParameter by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ConfigurationParameter : {}", id);
        configurationParameterRepository.deleteById(id);
    }
}
