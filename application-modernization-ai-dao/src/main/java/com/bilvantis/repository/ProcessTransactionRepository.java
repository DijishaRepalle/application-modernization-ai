package com.bilvantis.repository;

import com.bilvantis.model.ProcessTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcessTransactionRepository extends MongoRepository<ProcessTransaction, UUID> {
    @Query(value = "{}", sort = "{ 'timestamp' : -1 }")
    Optional<ProcessTransaction> findLatestRecord();
}
