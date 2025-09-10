package com.urbanservices.booking.service.impl;

import com.urbanservices.booking.exception.EmailAlreadyExistsException;
import com.urbanservices.booking.exception.PhoneAlreadyExistsException;
import com.urbanservices.booking.exception.ResourceNotFoundException;
import com.urbanservices.booking.service.EmailService;
import com.urbanservices.booking.model.User;
import com.urbanservices.booking.model.enums.UserRole;
import com.urbanservices.booking.repository.UserRepository;
import com.urbanservices.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, 
                          PasswordEncoder passwordEncoder,
                          EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }
        
        // Check if phone already exists
        if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
            throw new PhoneAlreadyExistsException("Phone number is already in use");
        }
        
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set default role if not set
        if (user.getRole() == null) {
            user.setRole(UserRole.ROLE_CUSTOMER);
        }
        
        // Set timestamps
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setLastLoginAt(null);
        
        // Generate email verification token
        user.setEmailVerificationToken(generateEmailVerificationToken());
        user.setEmailVerified(false);
        user.setActive(true);
        
        User savedUser = userRepository.save(user);
        
        // Send verification email (async)
        sendVerificationEmail(savedUser);
        
        return savedUser;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + username));
        
        if (!user.isActive()) {
            throw new ResourceNotFoundException("User is not active");
        }
        
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                user.getAuthorities()
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> findByRole(UserRole role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        // Soft delete
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user.getId()));
        
        // Check if email is being changed and already exists
        if (!existingUser.getEmail().equals(user.getEmail()) && 
            userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }
        
        // Check if phone is being changed and already exists
        if (user.getPhone() != null && 
            !user.getPhone().equals(existingUser.getPhone()) && 
            userRepository.existsByPhone(user.getPhone())) {
            throw new PhoneAlreadyExistsException("Phone number is already in use");
        }
        
        // Update fields
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setProfileImageUrl(user.getProfileImageUrl());
        existingUser.setUpdatedAt(LocalDateTime.now());
        
        // Role can only be updated by admin, so we don't update it here
        
        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
        
        // Send password change notification (async)
        sendPasswordChangeNotification(user);
    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        // Generate reset token
        String resetToken = generateResetToken();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(24)); // Token valid for 24 hours
        
        userRepository.save(user);
        
        // Send password reset email (async)
        sendPasswordResetEmail(user, resetToken);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .filter(u -> u.getResetTokenExpiry() != null && u.getResetTokenExpiry().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset token"));
        
        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
        
        // Send password reset confirmation (async)
        sendPasswordResetConfirmation(user);
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));
        
        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateLastLogin(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
        });
    }

    private String generateEmailVerificationToken() {
        return UUID.randomUUID().toString();
    }
    
    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }
    
    private void sendVerificationEmail(User user) {
        // In a real application, this would send an email with a verification link
        String verificationUrl = "https://yourapp.com/verify-email?token=" + user.getEmailVerificationToken();
        String subject = "Verify your email address";
        String content = String.format("Please click the link below to verify your email address:\n%s", verificationUrl);
        
        // This would be an async call to an email service
        emailService.sendEmail(user.getEmail(), subject, content);
    }
    
    private void sendPasswordResetEmail(User user, String resetToken) {
        // In a real application, this would send an email with a password reset link
        String resetUrl = "https://yourapp.com/reset-password?token=" + resetToken;
        String subject = "Password Reset Request";
        String content = String.format("To reset your password, please click the link below:\n%s\n\nThis link will expire in 24 hours.", resetUrl);
        
        // This would be an async call to an email service
        emailService.sendEmail(user.getEmail(), subject, content);
    }
    
    private void sendPasswordResetConfirmation(User user) {
        // In a real application, this would send a confirmation email
        String subject = "Password Updated Successfully";
        String content = "Your password has been successfully updated. If you did not make this change, please contact support immediately.";
        
        // This would be an async call to an email service
        emailService.sendEmail(user.getEmail(), subject, content);
    }
    
    private void sendPasswordChangeNotification(User user) {
        // In a real application, this would send a notification email
        String subject = "Password Changed";
        String content = "Your password has been changed. If you did not make this change, please contact support immediately.";
        
        // This would be an async call to an email service
        emailService.sendEmail(user.getEmail(), subject, content);
    }
}
