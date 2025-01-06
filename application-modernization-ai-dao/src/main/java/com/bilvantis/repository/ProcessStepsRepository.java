package com.bilvantis.repository;

import com.bilvantis.model.ProcessSteps;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProcessStepsRepository extends MongoRepository<ProcessSteps, UUID> {
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'process', 'localField': 'processId', 'foreignField': '_id', 'as': 'process' } }",
            "{ '$unwind': '$process' }",
            "{ '$match': { 'process.processName': ?0 } }"
    })
    List<ProcessSteps> findByProcessName(String processName);

}
