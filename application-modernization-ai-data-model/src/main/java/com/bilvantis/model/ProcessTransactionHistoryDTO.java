package com.bilvantis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessTransactionHistoryDTO extends BaseDTO {

    private UUID transactionHistoryId;
    private ProcessTransaction processTransaction;
    private String status;
    private Integer trial;
    private String jobId;
    private String failureMessage;
}
