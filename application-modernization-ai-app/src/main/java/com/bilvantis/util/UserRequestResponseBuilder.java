package com.bilvantis.util;

import com.bilvantis.model.ErrorResponse;
import com.bilvantis.model.UserResponseDTO;

import java.util.List;
import java.util.Objects;


public class UserRequestResponseBuilder {
    public static UserResponseDTO buildResponseDTO(Object body, Integer pageNumber, Integer size, Integer totalPages, List<ErrorResponse> errors, String status) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setBody(body);
        userResponseDTO.setStatus(status);
        userResponseDTO.setErrors(errors);
        userResponseDTO.setPageNo(pageNumber);
        userResponseDTO.setPerPage(size);
        userResponseDTO.setTotalPages(totalPages);
        return userResponseDTO;
    }

    public static UserResponseDTO buildResponseDTO(Object body, List<ErrorResponse> errors,
                                                   String status) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setBody(body);
        userResponseDTO.setStatus(status);
        userResponseDTO.setErrors(errors);
        return userResponseDTO;
    }

}
