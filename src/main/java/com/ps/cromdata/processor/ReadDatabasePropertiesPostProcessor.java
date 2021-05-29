package com.ps.cromdata.processor;

import com.ps.cromdata.config.Constants;
import com.ps.cromdata.util.CipherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Prior to the application context being refreshed this class load the configuration
 * from database and updates the application property values with this configuration.
 *
 * @author Leonardo M. Lopez
 */
public class ReadDatabasePropertiesPostProcessor implements EnvironmentPostProcessor {

    private final Logger log = LoggerFactory.getLogger(ReadDatabasePropertiesPostProcessor.class);
    private static final String CLASSNAME = "ReadDatabasePropertiesPostProcessor";
    private static final int DATABASE_CONNECTION_TIMEOUT_IN_SEC = 30;

    private static final String CREATE_UTM_CONFIGURATION_SECTION_SCRIPT =
        "CREATE TABLE IF NOT EXISTS \"public\".\"utm_configuration_section\" (\n" +
            "  \"id\" int8 NOT NULL,\n" +
            "  \"section\" varchar(255),\n" +
            "  \"description\" varchar(255),\n" +
            "  PRIMARY KEY (\"id\")\n" +
            ");\n" +
            "INSERT INTO \"public\".\"utm_configuration_section\" VALUES (1, 'Alert notifications', 'Configure Crondata email list sender') ON CONFLICT(id) DO NOTHING;\n" +
            "INSERT INTO \"public\".\"utm_configuration_section\" VALUES (2, 'email settings', 'Here you can configure all necessary parameters for email notifications') ON CONFLICT(id) DO NOTHING;";

    private static final String CREATE_UTM_CONFIGURATION_PARAMETER_SCRIPT =
        "CREATE TABLE IF NOT EXISTS \"public\".\"utm_configuration_parameter\" (\n" +
            "  \"id\" int8 NOT NULL,\n" +
            "  \"section_id\" int8 NOT NULL,\n" +
            "  \"conf_param_short\" varchar(100),\n" +
            "  \"conf_param_large\" varchar(255),\n" +
            "  \"conf_param_description\" varchar(255),\n" +
            "  \"conf_param_value\" varchar(255),\n" +
            "  \"conf_param_required\" bool,\n" +
            "  \"conf_param_datatype\" varchar(20),\n" +
            "  \"modification_time\" timestamp(6),\n" +
            "  \"modification_user\" varchar(255),\n" +
            "  PRIMARY KEY (\"id\")\n" +
            ");\n" +
            "INSERT INTO \"public\".\"utm_configuration_parameter\" VALUES (4, 1, 'crondata.mail.host', 'Mail Server Host', 'SMTP server host. For instance, `smtp.example.com`.', 'mail.crondata.com', 't', 'text', NULL, NULL) ON CONFLICT(id) DO NOTHING;\n" +
            "INSERT INTO \"public\".\"utm_configuration_parameter\" VALUES (5, 1, 'crondata.mail.port', 'Mail Server Port', 'SMTP server port', '587', 't', 'number', NULL, NULL) ON CONFLICT(id) DO NOTHING;\n" +
            "INSERT INTO \"public\".\"utm_configuration_parameter\" VALUES (6, 1, 'crondata.mail.username', 'Mail Server Username', 'Login user of the SMTP server', 'crondata@atlasintech.com', 't', 'text', NULL, NULL) ON CONFLICT(id) DO NOTHING;\n" +
            "INSERT INTO \"public\".\"utm_configuration_parameter\" VALUES (7, 1, 'crondata.mail.password', 'Mail Server Password', 'Login password of the SMTP server', 'e235g7b558bxbwd7', 't', 'password', NULL, NULL) ON CONFLICT(id) DO NOTHING;\n" +
            "INSERT INTO \"public\".\"utm_configuration_parameter\" VALUES (8, 1, 'crondata.mail.protocol', 'Mail Server Protocol', 'Protocol used by the SMTP server', 'smtp', 't', 'text', NULL, NULL) ON CONFLICT(id) DO NOTHING;\n" +
            "INSERT INTO \"public\".\"utm_configuration_parameter\" VALUES (9, 1, 'crondata.mail.properties.mail.smtp.auth', 'Smtp Auth', NULL, 't', 't', 'bool', NULL, NULL) ON CONFLICT(id) DO NOTHING;\n" +
            "INSERT INTO \"public\".\"utm_configuration_parameter\" VALUES (10, 1, 'crondata.mail.properties.mail.smtp.starttls.enable', 'Smtp Start TLS', NULL, 't', 't', 'bool', NULL, NULL) ON CONFLICT(id) DO NOTHING;\n" +
            "INSERT INTO \"public\".\"utm_configuration_parameter\" VALUES (11, 1, 'crondata.mail.properties.mail.smtp.ssl.trust', 'Smtp SSL Trust', NULL, 'mail.atlasinside.com', 't', 'text', NULL, NULL) ON CONFLICT(id) DO NOTHING;\n" +
            "INSERT INTO \"public\".\"utm_configuration_parameter\" VALUES (12, 1, 'crondata.mail.from', 'Crondata email address', 'Address from which emails are sent', NULL, 't', 'email', NULL, NULL) ON CONFLICT(id) DO NOTHING;\n" +
            "INSERT INTO \"public\".\"utm_configuration_parameter\" VALUES (13, 2, 'crondata.mail.list', 'On alert send notification to', 'Address which emails are sent when alert is firing', NULL, 't', 'list', NULL, NULL) ON CONFLICT(id) DO NOTHING;\n";

    /**
     * @see EnvironmentPostProcessor
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        final String ctx = CLASSNAME + ".postProcessEnvironment";
        try {
            Connection cnx = getDatabaseConnection(environment);

            if (cnx != null && !cnx.isValid(DATABASE_CONNECTION_TIMEOUT_IN_SEC))
                throw new Exception("Database connection is not valid");

            createUtmConfigurationSectionTable(cnx);
            createUtmConfigurationParameterTable(cnx);

            Map<String, Object> propertySource = buildApplicationProperties(cnx);
            environment.getPropertySources().addFirst(
                new MapPropertySource(Constants.CRONDATA_PROPERTY_SOURCE_NAME, propertySource));
            System.out.println(environment);
        } catch (Exception e) {
            String msg = ctx + ": " + e.getMessage();
            log.error(msg);
            throw new RuntimeException(msg);
        }
    }

    /**
     * Gets the configuration stored on database and create a map with it
     *
     * @return A map with property configuration from database
     */
    private Map<String, Object> buildApplicationProperties(Connection cnx) {
        final String ctx = CLASSNAME + ".buildApplicationProperties";

        try {
            String query = "SELECT param.conf_param_short, param.conf_param_datatype, param.conf_param_value, sec.\"id\" AS sectionId FROM utm_configuration_parameter param LEFT JOIN utm_configuration_section sec ON section_id = sec.\"id\"";

            ResultSet rs = cnx.prepareStatement(query).executeQuery();
            Map<String, Object> result = new HashMap<>();

            while (rs.next()) {
                String confParamShort = rs.getString(1);
                String confParamType = rs.getString(2);
                String confParamValue = rs.getString(3);
                String sectionId = rs.getString(4);

                try {
                    if (confParamType.equalsIgnoreCase("password"))
                        confParamValue = CipherUtil.decrypt(confParamValue, Constants.CONF_PARAM_PWD_TYPE_SECRET);
                } catch (Exception e) {
                    log.error(String.format("%1$s: Fail to decrypt value for parameter: %2$s with value: %3$s. Error is: %4$s",
                        ctx, confParamShort, confParamValue, e.getMessage()));
                }

                result.put(confParamShort, confParamValue);
            }

            rs.close();
            cnx.close();

            return result;
        } catch (Exception e) {
            String msg = ctx + ": " + e.getMessage();
            log.error(msg);
            throw new RuntimeException(msg);
        }
    }

    private void createUtmConfigurationSectionTable(Connection cnx) {
        final String ctx = CLASSNAME + ".createUtmConfigurationSectionTable";
        Statement st;
        try {
            st = cnx.createStatement();
            st.executeUpdate(CREATE_UTM_CONFIGURATION_SECTION_SCRIPT);
            st.close();
        } catch (Exception e) {
            String msg = ctx + ": " + e.getMessage();
            log.error(msg);
            e.printStackTrace();
            throw new RuntimeException(msg);
        }
    }

    private void createUtmConfigurationParameterTable(Connection cnx) {
        final String ctx = CLASSNAME + ".createUtmConfigurationParameterTable";
        Statement st;
        try {
            st = cnx.createStatement();
            st.executeUpdate(CREATE_UTM_CONFIGURATION_PARAMETER_SCRIPT);
            st.close();
        } catch (Exception e) {
            String msg = ctx + ": " + e.getMessage();
            log.error(msg);
            e.printStackTrace();
            throw new RuntimeException(msg);
        }
    }

    /**
     * @param env
     * @return
     */
    private Connection getDatabaseConnection(ConfigurableEnvironment env) {
        final String ctx = CLASSNAME + ".getDatabaseConnection";

        try {
            String dsUsername = env.getProperty("spring.datasource.username");
            String dsPassword = env.getProperty("spring.datasource.password");
            String dsUrl = env.getProperty("spring.datasource.url");
            String dsDriverClassName = "org.postgresql.Driver";

            Assert.hasText(dsUsername, "Missing datasource username configuration value");
            Assert.hasText(dsPassword, "Missing datasource password configuration value");
            Assert.hasText(dsUrl, "Missing datasource url configuration value");

            DataSource ds = DataSourceBuilder.create()
                .username(dsUsername).password(dsPassword).url(dsUrl)
                .driverClassName(dsDriverClassName).build();

            return ds.getConnection();
        } catch (Exception e) {
            String msg = ctx + ": " + e.getMessage();
            log.error(msg);
            throw new RuntimeException(msg);
        }
    }

}
