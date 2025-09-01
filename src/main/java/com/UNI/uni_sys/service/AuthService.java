package com.UNI.uni_sys.service;

import com.UNI.uni_sys.dto.LoginRequestDTO;
import com.UNI.uni_sys.dto.LoginResponseDTO;
import com.UNI.uni_sys.dto.UserRegistrationDTO;
import com.UNI.uni_sys.model.User;
import com.UNI.uni_sys.repository.UserRepository;
import com.UNI.uni_sys.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            
            User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
            
            if (user != null) {
                return new LoginResponseDTO(
                    token,
                    user.getUsername(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getRole().name(),
                    "Login successful"
                );
            } else {
                return new LoginResponseDTO(
                    null, null, null, null, null, null, "User not found"
                );
            }
            
        } catch (Exception e) {
            return new LoginResponseDTO(
                null, null, null, null, null, null, "Invalid username or password"
            );
        }
    }
    
    public LoginResponseDTO register(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            return new LoginResponseDTO(
                null, null, null, null, null, null, "Username already exists"
            );
        }
        
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            return new LoginResponseDTO(
                null, null, null, null, null, null, "Email already exists"
            );
        }
        
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setEmail(registrationDTO.getEmail());
        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setRole(registrationDTO.getRole() != null ? registrationDTO.getRole() : User.Role.STUDENT);
        user.setEnabled(true);
        
        User savedUser = userRepository.save(user);
        
        // Generate JWT token for the newly registered user
        String token = jwtUtil.generateToken(savedUser);
        
        return new LoginResponseDTO(
            token,
            savedUser.getUsername(),
            savedUser.getEmail(),
            savedUser.getFirstName(),
            savedUser.getLastName(),
            savedUser.getRole().name(),
            "Registration successful"
        );
    }
    
    public LoginResponseDTO createAdminUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            return new LoginResponseDTO(
                null, null, null, null, null, null, "Username already exists"
            );
        }
        
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            return new LoginResponseDTO(
                null, null, null, null, null, null, "Email already exists"
            );
        }
        
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setEmail(registrationDTO.getEmail());
        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setRole(User.Role.ADMIN);
        user.setEnabled(true);
        
        User savedUser = userRepository.save(user);
        
        // Generate JWT token for the newly created admin user
        String token = jwtUtil.generateToken(savedUser);
        
        return new LoginResponseDTO(
            token,
            savedUser.getUsername(),
            savedUser.getEmail(),
            savedUser.getFirstName(),
            savedUser.getLastName(),
            savedUser.getRole().name(),
            "Admin user created successfully"
        );
    }
}

