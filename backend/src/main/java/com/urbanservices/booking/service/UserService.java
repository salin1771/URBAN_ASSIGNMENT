package com.urbanservices.booking.service;

import com.urbanservices.booking.model.User;
import com.urbanservices.booking.model.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    
    User saveUser(User user);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findById(Long id);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);
    
    Page<User> findAll(Pageable pageable);
    
    Page<User> findByRole(UserRole role, Pageable pageable);
    
    void deleteUser(Long id);
    
    User updateUser(User user);
    
    void changePassword(Long userId, String currentPassword, String newPassword);
    
    void requestPasswordReset(String email);
    
    void resetPassword(String token, String newPassword);
    
    void verifyEmail(String token);
    
    void updateLastLogin(String email);
    
    @Override
    UserDetails loadUserByUsername(String username);
}
