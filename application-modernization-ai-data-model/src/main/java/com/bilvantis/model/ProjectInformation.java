package com.bilvantis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Projects")
public class ProjectInformation extends BaseDTO {
    @Id
    private String ProjectId;
    private String ProjectName;
    private String ProjectDescription;
    private String ProjectStatus;
    private String programmingLanguage;
    private String version;
    private String projectCode;
    private String repoUrl;
    private String token;
    // One-to-Many Relationship
    @DBRef
    private List<UserInformation> taggedUsers;

}