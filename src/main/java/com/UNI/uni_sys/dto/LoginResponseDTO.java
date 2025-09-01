package com.UNI.uni_sys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    
    private String token;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String message;
}

