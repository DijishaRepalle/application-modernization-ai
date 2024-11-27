package com.bilvantis.repository;

import com.bilvantis.model.ProjectInformation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectInformationRepository extends MongoRepository<ProjectInformation, String> {
}