package com.bilvantis.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserResponseDTO extends PageDetailsDTO {
    private Object body;
    private String status;
    private List<ErrorResponse> errors;
}