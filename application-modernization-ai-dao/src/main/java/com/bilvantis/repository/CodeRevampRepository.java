package com.bilvantis.repository;

import com.bilvantis.model.CodeRevamp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CodeRevampRepository extends MongoRepository<CodeRevamp, UUID> {
}
