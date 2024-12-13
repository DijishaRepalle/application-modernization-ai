package com.bilvantis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloneRequest {

    private String token;

    private String ProjectCode;
}
