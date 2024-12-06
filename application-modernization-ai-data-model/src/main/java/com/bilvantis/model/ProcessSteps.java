package com.bilvantis.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "ProcessSteps")
@Data
public class ProcessSteps {

    @Id
    private UUID stepId;
    @DBRef
    private Process processId;
    private String stepName;
    private String description;
    private int stepSequence;
}
