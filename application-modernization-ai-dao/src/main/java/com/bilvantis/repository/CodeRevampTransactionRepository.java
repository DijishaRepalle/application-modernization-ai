package com.bilvantis.repository;

import com.bilvantis.model.CodeRevampTranscation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CodeRevampTransactionRepository extends MongoRepository<CodeRevampTranscation, UUID> {
    @Query(value = "{}", sort = "{ 'timestamp' : -1 }")
    Optional<CodeRevampTranscation> findLatestRecord();
}
