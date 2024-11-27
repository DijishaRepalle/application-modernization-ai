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
    private String id;
    private String name;
    private String description;
    private String ownerId;
    private String status;
    private String language;
    private String version;
    // One-to-Many Relationship
    @DBRef
    private List<UserInformation> taggedUsers;

}