package com.bilvantis.repository;

import com.bilvantis.model.CodeRevampProcess;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CodeRevampProcessRepository extends MongoRepository<CodeRevampProcess, UUID> {
}