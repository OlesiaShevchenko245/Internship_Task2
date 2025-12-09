package com.cosmorum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {
    private Long id;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Nationality is required")
    private String nationality;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class AuthorSummaryDTO {
    private Long id;
    private String firstName;
    private String lastName;
}