package com.ps.cromdata.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Properties specific to Cron Data Backend.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private final List<String> alertNotificationMails = new ArrayList<>();
    private final ApplicationProperties.MailConfig mailConfig = new ApplicationProperties.MailConfig();

    public MailConfig getMailConfig() {
        return mailConfig;
    }

    public List<String> getAlertNotificationMails() {
        return alertNotificationMails;
    }

    public static class MailConfig {
        private String user;
        private String pass;
        private String host;
        private String port;

        public MailConfig() {
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }
    }
}
