package com.bilvantis.repository;

import com.bilvantis.model.Process;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProcessRepository extends MongoRepository<Process, UUID> {


}
