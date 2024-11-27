package com.bilvantis.exception;

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

    public static UserResponseDTO buildResponseDTO(Object body, Integer pageNumber, Integer size, Integer totalPages, List<ErrorResponse> errors, String status, String sortingOrder) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setBody(body);
        userResponseDTO.setStatus(status);
        userResponseDTO.setErrors(errors);
        userResponseDTO.setSortingOrder(sortingOrder);
        if (Objects.nonNull(pageNumber) && Objects.nonNull(size)) {
            userResponseDTO.setPageNo(pageNumber);
            userResponseDTO.setPerPage(size);
            userResponseDTO.setTotalPages(totalPages);
        } else {
            userResponseDTO.setPageNo(null);
            userResponseDTO.setPerPage(null);
            userResponseDTO.setTotalPages(null);
        }
        return userResponseDTO;
    }

}