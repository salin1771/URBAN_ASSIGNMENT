package com.urbanservices.booking.service;

import com.urbanservices.booking.dto.CustomerDto;
import com.urbanservices.booking.dto.auth.JwtAuthenticationResponse;
import com.urbanservices.booking.dto.auth.LoginRequest;
import com.urbanservices.booking.dto.auth.CustomerRegistrationRequest;
import com.urbanservices.booking.dto.auth.ProfessionalRegistrationRequest;
import com.urbanservices.booking.exception.EmailAlreadyExistsException;
import com.urbanservices.booking.exception.ResourceNotFoundException;
import com.urbanservices.booking.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import com.urbanservices.booking.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    private final ProfessionalService professionalService;
    private final CustomerService customerService;

    public AuthService(AuthenticationManager authenticationManager,
                      JwtTokenProvider tokenProvider,
                      UserService userService,
                      ProfessionalService professionalService,
                      CustomerService customerService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.professionalService = professionalService;
        this.customerService = customerService;
    }

    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Get the authenticated user details
        User user = (User) authentication.getPrincipal();
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        String jwt = tokenProvider.generateToken(userDetails);
        String refreshToken = tokenProvider.generateRefreshToken(userDetails);

        return JwtAuthenticationResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(tokenProvider.getJwtExpirationInMs() / 1000) // Convert to seconds
                .role(user.getRole().name())
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public JwtAuthenticationResponse registerCustomer(CustomerRegistrationRequest registrationRequest) {
        // Check if email already exists
        if (userService.existsByEmail(registrationRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        // Map registration request to CustomerDto
        CustomerDto customerDto = new CustomerDto();
        customerDto.setEmail(registrationRequest.getEmail());
        // Note: CustomerDto doesn't have a password field, it will be set in the service
        // Split the name into first and last name
        String[] nameParts = registrationRequest.getName().split(" ", 2);
        customerDto.setFirstName(nameParts[0]);
        customerDto.setLastName(nameParts.length > 1 ? nameParts[1] : "");
        customerDto.setPhone(registrationRequest.getPhone());

        // Create new customer
        customerService.createCustomer(customerDto);
        
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registrationRequest.getEmail(),
                        registrationRequest.getPassword()
                )
        );
        
        // Generate JWT token
        // Get the authenticated user details
        User user = (User) authentication.getPrincipal();
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        String accessToken = tokenProvider.generateToken(userDetails);
        String refreshToken = tokenProvider.generateRefreshToken(userDetails);
        
        return JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    public JwtAuthenticationResponse registerProfessional(ProfessionalRegistrationRequest registrationRequest) {
        // Check if email already exists
        if (userService.existsByEmail(registrationRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        // Create new professional
        return professionalService.registerProfessional(registrationRequest);
    }

    public JwtAuthenticationResponse refreshToken(String refreshToken) {
        // First validate the token format and expiration
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String email = tokenProvider.getUsernameFromJWT(refreshToken);
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Get the user details from the database
        UserDetails userDetails = userService.loadUserByUsername(email);
        
        // Generate new tokens
        String newJwt = tokenProvider.generateToken(userDetails);
        String newRefreshToken = tokenProvider.generateRefreshToken(userDetails);

        return JwtAuthenticationResponse.builder()
                .accessToken(newJwt)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(tokenProvider.getJwtExpirationInMs() / 1000)
                .role(user.getRole().name())
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public void logout(String token) {
        // In a stateless application, the client is responsible for removing the token
        // But we can implement token blacklisting or other cleanup here if needed
    }
}
