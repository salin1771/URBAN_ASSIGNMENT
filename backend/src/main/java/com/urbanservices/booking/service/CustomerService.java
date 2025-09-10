package com.urbanservices.booking.service;

import com.urbanservices.booking.dto.CustomerDto;
import com.urbanservices.booking.exception.ResourceAlreadyExistsException;
import com.urbanservices.booking.exception.ResourceNotFoundException;
import com.urbanservices.booking.mapper.CustomerMapper;
import com.urbanservices.booking.model.Customer;
import com.urbanservices.booking.model.enums.UserRole;
import com.urbanservices.booking.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        return customerMapper.toDto(customer);
    }
    
    @Transactional
    public CustomerDto createCustomer(CustomerDto customerDto) {
        // Check if email already exists
        if (customerRepository.existsByEmail(customerDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Customer", "email", customerDto.getEmail());
        }
        
        // Map DTO to entity
        Customer customer = customerMapper.toEntity(customerDto);
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customer.setRole(UserRole.ROLE_CUSTOMER);
        customer.setEnabled(true);
        
        // Save the customer
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }
    
    @Transactional
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        // Find existing customer
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        
        // Update fields
        String fullName = customerDto.getFirstName() + " " + customerDto.getLastName();
        existingCustomer.setName(fullName);
        existingCustomer.setPhone(customerDto.getPhone());
        
        // Only update password if it's provided
        if (customerDto.getPassword() != null && !customerDto.getPassword().isEmpty()) {
            existingCustomer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        }
        
        // Save the updated customer
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toDto(updatedCustomer);
    }
    
    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer", "id", id);
        }
        customerRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public CustomerDto getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));
        return customerMapper.toDto(customer);
    }
}
