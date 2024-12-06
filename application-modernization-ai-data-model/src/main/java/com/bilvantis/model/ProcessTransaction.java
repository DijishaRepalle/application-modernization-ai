package com.bilvantis.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Document(collection = "process_transaction")
public class ProcessTransaction extends BaseDTO {

    @Id
    private String processTransactionId;
    private String stepId;
    private String stepName;
    private String projectCode;
    private String status;
    private Integer trial;
    private String jobId;
    private String latestFailureMessage;

}