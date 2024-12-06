package com.bilvantis.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "process")
@Data
public class Process {

    @Id
    private String processId;
    private String processName;
    private String description;
}
