package com.bilvantis.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document(collection = "code_revamp_transaction_history")
public class CodeRevampProcessTransactionHistory extends BaseDTO {
    @Id
    private UUID codeRevampProcessTransactionHistoryId;
    private String codeRevampTransactionId;
    private String status;
    private Integer trial;
    private String jobId;
    private String failureMessage;
}