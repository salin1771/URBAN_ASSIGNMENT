package com.urbanservices.booking.service.impl;

import com.urbanservices.booking.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Locale;

/**
 * Implementation of EmailService using Spring's JavaMailSender.
 * This is a simplified version. In a production environment, you would want to:
 * 1. Use a proper email template engine (like Thymeleaf or FreeMarker)
 * 2. Handle email sending asynchronously
 * 3. Implement retry mechanisms for failed emails
 * 4. Log email sending status
 * 5. Potentially use a queue for sending emails in bulk
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    
    @Override
    @Async
    public boolean sendEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            
            mailSender.send(message);
            logger.info("Email sent to: {}", to);
            return true;
        } catch (MailException e) {
            logger.error("Failed to send email to: {}", to, e);
            return false;
        }
    }
    
    @Override
    @Async
    public boolean sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = isHtml
            
            mailSender.send(mimeMessage);
            logger.info("HTML email sent to: {}", to);
            return true;
        } catch (MessagingException | MailException e) {
            logger.error("Failed to send HTML email to: {}", to, e);
            return false;
        }
    }
    
    @Override
    @Async
    public boolean sendTemplateEmail(String to, String subject, String templateName, Object templateModel) {
        try {
            // Prepare the evaluation context
            Context context = new Context(Locale.getDefault());
            context.setVariable("model", templateModel);
            
            // Process the template
            String htmlContent = templateEngine.process(templateName, context);
            
            // Send the email
            return sendHtmlEmail(to, subject, htmlContent);
        } catch (Exception e) {
            logger.error("Failed to process email template: {}", templateName, e);
            return false;
        }
    }
    
    /**
     * Sends a verification email to the user.
     * This is just an example of how you might structure a specific email method.
     */
    @Async
    public void sendVerificationEmail(String to, String name, String verificationUrl) {
        String subject = "Verify your email address";
        String templateName = "email/verification";
        
        // Create a model for the template
        EmailVerificationModel model = new EmailVerificationModel(name, verificationUrl);
        
        // Send the email using the template
        sendTemplateEmail(to, subject, templateName, model);
    }
    
    /**
     * Sends a password reset email to the user.
     */
    @Async
    public void sendPasswordResetEmail(String to, String name, String resetUrl) {
        String subject = "Password Reset Request";
        String templateName = "email/password-reset";
        
        // Create a model for the template
        PasswordResetModel model = new PasswordResetModel(name, resetUrl);
        
        // Send the email using the template
        sendTemplateEmail(to, subject, templateName, model);
    }
    
    /**
     * Sends a booking confirmation email to the customer.
     */
    @Async
    public void sendBookingConfirmation(String to, String name, BookingConfirmationModel bookingDetails) {
        String subject = String.format("Booking Confirmation - #%s", bookingDetails.getBookingNumber());
        String templateName = "email/booking-confirmation";
        
        // Send the email using the template
        sendTemplateEmail(to, subject, templateName, bookingDetails);
    }
    
    // Inner classes for email models
    
    public static class EmailVerificationModel {
        private final String name;
        private final String verificationUrl;
        
        public EmailVerificationModel(String name, String verificationUrl) {
            this.name = name;
            this.verificationUrl = verificationUrl;
        }
        
        // Getters for Thymeleaf
        public String getName() { return name; }
        public String getVerificationUrl() { return verificationUrl; }
    }
    
    public static class PasswordResetModel {
        private final String name;
        private final String resetUrl;
        
        public PasswordResetModel(String name, String resetUrl) {
            this.name = name;
            this.resetUrl = resetUrl;
        }
        
        // Getters for Thymeleaf
        public String getName() { return name; }
        public String getResetUrl() { return resetUrl; }
    }
    
    public static class BookingConfirmationModel {
        private final String bookingNumber;
        private final String serviceName;
        private final String professionalName;
        private final String dateTime;
        private final String address;
        private final double totalAmount;
        
        public BookingConfirmationModel(String bookingNumber, String serviceName, 
                                      String professionalName, String dateTime, 
                                      String address, double totalAmount) {
            this.bookingNumber = bookingNumber;
            this.serviceName = serviceName;
            this.professionalName = professionalName;
            this.dateTime = dateTime;
            this.address = address;
            this.totalAmount = totalAmount;
        }
        
        // Getters for Thymeleaf
        public String getBookingNumber() { return bookingNumber; }
        public String getServiceName() { return serviceName; }
        public String getProfessionalName() { return professionalName; }
        public String getDateTime() { return dateTime; }
        public String getAddress() { return address; }
        public double getTotalAmount() { return totalAmount; }
    }
}
