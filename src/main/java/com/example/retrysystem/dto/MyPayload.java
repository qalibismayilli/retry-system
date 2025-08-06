package com.example.retrysystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyPayload implements Serializable {
    private String operation;
    private String endpoint;
    private String requestBody;
    private int httpStatus;
    private String errorMessage;
    private LocalDateTime timestamp;
}
