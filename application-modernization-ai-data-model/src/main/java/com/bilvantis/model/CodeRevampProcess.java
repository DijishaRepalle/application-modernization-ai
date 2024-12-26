package com.bilvantis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "code_revamp_process")
@Data
public class CodeRevampProcess {
    @Id
    private String codeRevampProcessId;
    private String processName;
    private String description;
}
