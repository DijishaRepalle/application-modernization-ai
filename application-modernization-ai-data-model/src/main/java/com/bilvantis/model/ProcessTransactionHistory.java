package com.bilvantis.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "process_transaction_history")
public class ProcessTransactionHistory extends BaseDTO {
    @Id
    private UUID transactionHistoryId;
    private String processTransactionId;
    private String status;
    private Integer trial;
    private String jobId;
    private String failureMessage;

}