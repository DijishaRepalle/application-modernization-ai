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
@Document(collection = "ProcessTranscationHistory")
public class ProcessTransactionHistory extends BaseDTO {
    @Id
    private UUID transactionHistoryId;
    @DBRef
    private ProcessTransaction processTransaction;
    private String status;
    private Integer trial;
    private String jobId;
    private String failureMessage;

}