package com.ps.cromdata.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ANONYMOUS_USER = "anonymoususer";


    // CONFIG KEYS
    public static final String CRONDATA_PROPERTY_SOURCE_NAME = "crondataConfiguration";
    public static final String PROP_MAIL_FROM = "crondata.mail.from";
    public static final String PROP_MAIL_MODULE_NAME = "mail";
    public static final String PROP_MAIL_HOST = "crondata.mail.host";
    public static final String PROP_MAIL_PORT = "crondata.mail.port";
    public static final String PROP_MAIL_USER = "crondata.mail.username";
    public static final String PROP_MAIL_PASS = "crondata.mail.password";
    public static final String PROP_MAIL_PROTOCOL = "crondata.mail.protocol";
    public static final String PROP_MAIL_SMTP_SSL = "crondata.mail.properties.mail.smtp.starttls.enable";
    public static final String PROP_MAIL_SMTP_SSL_TRUST = "crondata.mail.properties.mail.smtp.ssl.trust";
    public static final String PROP_MAIL_FROM_USER = "crondata.jhipster.mail.from";

    public static final String PROP_ALERT_MAIL_PASS = "crondata.mail.list";
    public static final String PROP_MAIL_ADDRESS_TO_NOTIFY = "utmstack.mail.addressToNotify";

    public static final String PROP_ALERT_MODULE_NAME = "alert";
    public static final String PROP_MAIL_LIST = "alert.email.list";

    public static final String CLIENT_ENCRYPT_SECRET = "y5wmc02lbUtlxtzYuSHIObnBPo90RjEffYphOhqNZJYm0m8i4DQIC39hg66XgA0jofq0kEB9dqj1Ag9uJP2cH0jNUdyYBo7PrlDB";
    public static final String CONF_PARAM_PWD_TYPE_SECRET = "Cron.Pwd-53cr3t.5t4k!_3mpTy*";
    public static final String GRAFANA_URL = "http://localhost:3000";
    public static final String PROMETHEUS_URL = "http://prometheus:9090";

    private Constants() {
    }
}
