package com.bilvantis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDTO {
    private String createdBy;

    private Date createdDate;

    private String updatedBy;

    private Date updatedDate;

    private Boolean isActive;
}