package com.bilvantis.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @DBRef
    private ProjectInformation projectId;
    private String status;
    private Integer trial;
    private String jobId;
    private String latestFailureMessage;

}
