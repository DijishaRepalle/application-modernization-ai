package com.bilvantis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "code_revamp_steps")
public class CodeRevampProcessSteps {

    @Id
    private String codeRevampStepId;
    private String codeRevampProcessId;
    private String stepName;
    private String description;
    private int stepSequence;
}