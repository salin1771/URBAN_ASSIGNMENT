package com.urbanservices.booking.service;

/**
 * Service for sending emails.
 * In a real application, this would integrate with an email provider like SendGrid, Mailchimp, or AWS SES.
 */
public interface EmailService {
    
    /**
     * Sends an email to the specified recipient.
     *
     * @param to the email address of the recipient
     * @param subject the subject of the email
     * @param content the content of the email (can be plain text or HTML)
     * @return true if the email was sent successfully, false otherwise
     */
    boolean sendEmail(String to, String subject, String content);
    
    /**
     * Sends an email to the specified recipient with HTML content.
     *
     * @param to the email address of the recipient
     * @param subject the subject of the email
     * @param htmlContent the HTML content of the email
     * @return true if the email was sent successfully, false otherwise
     */
    boolean sendHtmlEmail(String to, String subject, String htmlContent);
    
    /**
     * Sends an email to the specified recipient with a template.
     *
     * @param to the email address of the recipient
     * @param subject the subject of the email
     * @param templateName the name of the template to use
     * @param templateModel the model to use for populating the template
     * @return true if the email was sent successfully, false otherwise
     */
    boolean sendTemplateEmail(String to, String subject, String templateName, Object templateModel);
}
