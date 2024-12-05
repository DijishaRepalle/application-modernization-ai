package com.bilvantis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessTransactionDTO extends BaseDTO {
    private UUID processTransactionId;
    private ProcessSteps stepId;
    private ProjectInformation projectId;
    private String status;
    private Integer trial;
    private String jobId;
    private String latestFailureMessage;
}
