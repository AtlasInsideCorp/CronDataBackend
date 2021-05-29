package com.ps.cromdata.service;

import com.ps.cromdata.config.Constants;
import com.ps.cromdata.domain.Alerts;
import com.ps.cromdata.domain.User;

import io.github.jhipster.config.JHipsterProperties;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private static final String CLASS_NAME = "mailSerice";
    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private final ApplicationPropertyService applicationPropertyService;

    public MailService(JHipsterProperties jHipsterProperties,
                       ApplicationPropertyService applicationPropertyService,
                       MessageSource messageSource,
                       JavaMailSender javaMailSender,
                       SpringTemplateEngine templateEngine) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.applicationPropertyService = applicationPropertyService;
    }

    public JavaMailSenderImpl getMailConfig() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(applicationPropertyService.getProperty(Constants.PROP_MAIL_HOST));
        mailSender.setPort(Integer.parseInt(applicationPropertyService.getProperty(Constants.PROP_MAIL_PORT)));
        mailSender.setDefaultEncoding("");
        mailSender.setUsername(applicationPropertyService.getProperty(Constants.PROP_MAIL_USER));
        mailSender.setPassword(applicationPropertyService.getProperty(Constants.PROP_MAIL_PASS));

        Properties props = mailSender.getJavaMailProperties();
        props.put("spring.mail.protocol", applicationPropertyService.getProperty(Constants.PROP_MAIL_PROTOCOL));
        props.put("mail.smtps.starttls.enable", applicationPropertyService.getProperty(Constants.PROP_MAIL_SMTP_SSL).equals("true"));
        props.put("mail.smtp.auth", applicationPropertyService.getProperty(Constants.PROP_MAIL_SMTP_SSL_TRUST));

        return mailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);
        JavaMailSenderImpl sender = this.getMailConfig();
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = sender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(applicationPropertyService.getProperty(Constants.PROP_MAIL_FROM));
            message.setSubject(subject);
            message.addInline("logo",
                new ClassPathResource("logo.png"));
            message.setText(content, isHtml);
            sender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }

    @Async
    public void sendAlertEmail(Alerts alert) {
        final String ctx = CLASS_NAME + ".sendAlertEmail";
        try {
            JavaMailSenderImpl sender = getMailConfig();
            if (alert == null)
                return;
            Locale locale = Locale.forLanguageTag("en");
            Context context = new Context(locale);

            context.setVariable("summary", alert);
            context.setVariable("severity", alert.getSeverity());
            context.setVariable("description", alert.getDescription());
            context.setVariable("date", alert.getReceivedAt());


            final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject(alert.getSummary());
            message.addAttachment("logo.png", new ClassPathResource("logo.png"));
            message.setFrom(applicationPropertyService.getProperty(Constants.PROP_MAIL_FROM));
            message.setTo(applicationPropertyService.getProperty(Constants.PROP_MAIL_LIST));

            final String htmlContent = templateEngine.process("mail/alertEmail", context);
            message.setText(htmlContent, true);
            sender.send(mimeMessage);
        } catch (Exception e) {
            String msg = String.format("%1$s: Email could not be sent to %2$s", ctx, e.getMessage());
        }
    }
}
