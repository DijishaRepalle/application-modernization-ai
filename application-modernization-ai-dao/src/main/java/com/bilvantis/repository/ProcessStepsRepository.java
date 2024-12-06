package com.bilvantis.repository;

import com.bilvantis.model.ProcessSteps;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface ProcessStepsRepository extends MongoRepository<ProcessSteps, UUID> {

    public Set<ProcessSteps> findByProcessId_processName(String processName);
}
