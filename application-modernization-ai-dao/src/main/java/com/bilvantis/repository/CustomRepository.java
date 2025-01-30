package com.bilvantis.repository;


import com.bilvantis.model.ProjectInformation;

import java.util.Optional;

public interface CustomRepository {


    Optional<ProjectInformation> findByProjectCode(String projectCode);

    String findHighestProjectCode();
}
