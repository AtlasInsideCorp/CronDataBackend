package com.ps.cromdata.service;

import com.ps.cromdata.config.Constants;
import com.ps.cromdata.domain.UtmConfigurationParameter;
import com.ps.cromdata.repository.UtmConfigurationParameterRepository;
import com.ps.cromdata.util.CipherUtil;
import com.ps.cromdata.domain.UtmConfigurationParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing UtmConfigurationParameter.
 */
@Service
@Transactional
public class UtmConfigurationParameterService {

    private static final String CLASSNAME = "UtmConfigurationParameterService";
    private final Logger log = LoggerFactory.getLogger(UtmConfigurationParameterService.class);

    private final UtmConfigurationParameterRepository utmConfigurationParameterRepository;
    private final ApplicationPropertyService applicationPropertyService;
    private final UserService userService;

    public UtmConfigurationParameterService(UtmConfigurationParameterRepository utmConfigurationParameterRepository,
                                            ApplicationPropertyService applicationPropertyService,
                                            UserService userService) {
        this.utmConfigurationParameterRepository = utmConfigurationParameterRepository;
        this.applicationPropertyService = applicationPropertyService;
        this.userService = userService;
    }

    /**
     * Save a utmConfigurationParameter.
     *
     * @param parameter the entity to save
     * @return the persisted entity
     */
    public UtmConfigurationParameter save(@Valid UtmConfigurationParameter parameter) {
        final String ctx = CLASSNAME + ".";

        try {
            applicationPropertyService.updatePropertySource(parameter.getConfParamShort(), parameter.getConfParamValue());

            if (parameter.getConfParamDatatype().equalsIgnoreCase("password"))
                parameter.setConfParamValue(CipherUtil.encrypt(parameter.getConfParamValue(), Constants.CONF_PARAM_PWD_TYPE_SECRET));

            return utmConfigurationParameterRepository.save(parameter);
        } catch (Exception e) {
            throw new RuntimeException(ctx + ": " + e.getMessage());
        }
    }

    /**
     * @param parameters
     */
    public void saveAll(List<UtmConfigurationParameter> parameters) throws Exception {
        final String ctx = CLASSNAME + ".saveAll";

        try {
            for (UtmConfigurationParameter parameter : parameters) {
                applicationPropertyService.updatePropertySource(parameter.getConfParamShort(),
                    parameter.getConfParamValue());

                if (parameter.getConfParamDatatype().equalsIgnoreCase("password"))
                    parameter.setConfParamValue(CipherUtil.encrypt(parameter.getConfParamValue(), Constants.CONF_PARAM_PWD_TYPE_SECRET));
            }

            utmConfigurationParameterRepository.saveAll(parameters);

        } catch (Exception e) {
            throw new RuntimeException(ctx + ": " + e.getMessage());
        }
    }

    /**
     * Get all the utmConfigurationParameters.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UtmConfigurationParameter> findAll(Pageable pageable) {
        log.debug("Request to get all UtmConfigurationParameters");
        return utmConfigurationParameterRepository.findAll(pageable);
    }


    /**
     * Get one utmConfigurationParameter by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<UtmConfigurationParameter> findOne(Long id) {
        log.debug("Request to get UtmConfigurationParameter : {}", id);
        return utmConfigurationParameterRepository.findById(id);
    }

    /**
     * Delete the utmConfigurationParameter by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UtmConfigurationParameter : {}", id);
        utmConfigurationParameterRepository.deleteById(id);
    }
}
