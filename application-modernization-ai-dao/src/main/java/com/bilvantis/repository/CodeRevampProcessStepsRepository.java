package com.bilvantis.repository;

import com.bilvantis.model.CodeRevampProcessSteps;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface CodeRevampProcessStepsRepository extends MongoRepository<CodeRevampProcessSteps, UUID> {
  @Aggregation(pipeline = {
          "{ '$lookup': { 'from': 'codeRevampProcess', 'localField': 'codeRevampProcessId', 'foreignField': '_id', 'as': 'process' } }",
          "{ '$unwind': '$process' }",
          "{ '$match': { 'process.processName': ?0 } }"
  })
  List<CodeRevampProcessSteps> findByProcessName(String processName);
}