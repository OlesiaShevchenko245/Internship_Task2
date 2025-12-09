package com.cosmorum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObservationDTO {
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Observation time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime observationTime;
    
    @NotNull(message = "Author ID is required")
    private Long authorId;
    
    private AuthorDTO author;
    
    private List<String> celestialObjects;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ObservationListDTO {
    private Long id;
    private String name;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime observationTime;
    
    private String authorName;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ObservationFilterRequest {
    private Long authorId;
    private String name;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;
    
    private Integer page = 1;
    private Integer size = 20;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ObservationListResponse {
    private List<ObservationListDTO> list;
    private Integer totalPages;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UploadResponse {
    private int successCount;
    private int failureCount;
    private List<String> errors;
}