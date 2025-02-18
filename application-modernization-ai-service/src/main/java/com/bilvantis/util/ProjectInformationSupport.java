package com.bilvantis.util;

import com.bilvantis.model.ProjectInformation;
import com.bilvantis.model.ProjectInformationDTO;
import com.bilvantis.model.UserInformation;
import com.bilvantis.model.UserInformationDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
public class ProjectInformationSupport {


    public static ProjectInformationDTO convertProjectEntityToProjectDTO(ProjectInformation project) {
        ProjectInformationDTO projectDTO = new ProjectInformationDTO();
        projectDTO.setProjectId(project.getProjectId());
        projectDTO.setProjectName(project.getProjectName());
        projectDTO.setProjectDescription(project.getProjectDescription());
        projectDTO.setProjectStatus(project.getProjectStatus());
        projectDTO.setProgrammingLanguage(project.getProgrammingLanguage());
        projectDTO.setVersion(project.getVersion());
        projectDTO.setCreatedDate(LocalDateTime.now());
        projectDTO.setCreatedBy(project.getCreatedBy());
        projectDTO.setRepoUrl(project.getRepoUrl());
        projectDTO.setProjectCode(project.getProjectCode());
        projectDTO.setToken(project.getToken());
        if (project.getTaggedUsers() != null) {
            List<UserInformationDTO> userDTOList = project.getTaggedUsers().stream()
                    .filter(Objects::nonNull).map(UserInformationSupport::convertUserEntityTOUserDTO).toList();
            projectDTO.setTaggedUsers(userDTOList);
        }
        return projectDTO;
    }


    public static ProjectInformation convertProjectDTOToProjectEntity(ProjectInformationDTO projectDTO) {
        ProjectInformation project = new ProjectInformation();
        project.setProjectId(projectDTO.getProjectId());
        project.setProjectName(projectDTO.getProjectName());
        project.setProjectDescription(projectDTO.getProjectDescription());
        project.setProjectStatus(projectDTO.getProjectStatus());
        project.setProgrammingLanguage(projectDTO.getProgrammingLanguage());
        project.setVersion(projectDTO.getVersion());
        project.setCreatedDate(LocalDateTime.now());
        project.setCreatedBy(projectDTO.getCreatedBy());
        project.setRepoUrl(projectDTO.getRepoUrl());
        String generatedProjectCode = ProjectCodeGenerator.generateProjectCode();
        project.setProjectCode(generatedProjectCode);
        project.setToken(projectDTO.getToken());
        if (projectDTO.getTaggedUsers() != null) {
            List<UserInformation> userList = projectDTO.getTaggedUsers().stream()
                    .filter(Objects::nonNull).map(UserInformationSupport::convertUsersDTOTOUsersEntity).toList();
            project.setTaggedUsers(userList);
        }
        return project;
    }
}