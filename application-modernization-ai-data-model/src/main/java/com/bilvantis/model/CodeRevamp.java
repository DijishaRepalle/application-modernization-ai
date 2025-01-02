package com.bilvantis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "code_revamp")
public class CodeRevamp extends BaseDTO {
    @Id
    private String codeRevampId;

    private String dependencyMatrix;

    private String fileLevelMatrix;

    private Integer migrationVersion;

    private String projectCode;


}