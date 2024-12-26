package com.bilvantis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "code_revamp_transcation")
@Data
public class CodeRevampTranscation extends BaseDTO {
    @Id
    private String codeRevampTransactionId;
    private String codeRevampStepId;
    private String stepName;
    private String projectCode;
    private String status;
    private Integer trial;
    private String jobId;
    private String latestFailureMessage;
}