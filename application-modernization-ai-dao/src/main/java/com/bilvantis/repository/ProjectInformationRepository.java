package com.bilvantis.repository;

import com.bilvantis.model.ProjectInformation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProjectInformationRepository extends MongoRepository<ProjectInformation, String> {
    Optional<ProjectInformation> findByProjectCode(String projectCode);
}