package com.bilvantis.service;

import com.bilvantis.model.ProjectInformation;
import com.bilvantis.model.ProjectInformationDTO;

import java.util.List;

public interface ProjectInformationService<I extends ProjectInformation, J extends ProjectInformationDTO> {

    J createProject(J projectInformationDTO);

    List<J> getAllProjectInformation();

    void deleteProjectById(String id);

    J updateProjectByProjectId(String projectId, J projectInformationDTO);

    J addUsersToProject(String projectCode, List<String> userIds);
}