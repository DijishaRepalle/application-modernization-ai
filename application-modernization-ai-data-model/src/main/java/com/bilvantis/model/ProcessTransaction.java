package com.bilvantis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "ProcessTranscation")
public class ProcessTransaction extends BaseDTO {

    @Id
    private UUID processTransactionId;
    @DBRef
    private ProcessSteps stepId;
    private String projectCode;
    private String status;
    private Integer trial;
    private String jobId;
    private String latestFailureMessage;

}