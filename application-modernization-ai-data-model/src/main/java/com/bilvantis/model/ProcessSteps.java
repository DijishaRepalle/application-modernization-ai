package com.bilvantis.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "ProcessSteps")
public class ProcessSteps extends BaseDTO {
    @Id
    private UUID processStepId;
}

