package com.bilvantis.util;

import com.bilvantis.model.ProjectInformation;
import com.bilvantis.model.ProjectInformationDTO;
import com.bilvantis.model.UserInformation;
import com.bilvantis.model.UserInformationDTO;

import java.util.List;

public class ProjectInformationSupport {
    public static ProjectInformationDTO convertProjectEntityToProjectDTO(ProjectInformation project) {
        ProjectInformationDTO projectDTO = new ProjectInformationDTO();
        projectDTO.setId(project.getId());
        projectDTO.setName(project.getName());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setStatus(project.getStatus());
        projectDTO.setLanguage(project.getLanguage());
        projectDTO.setOwnerId(project.getOwnerId());
        projectDTO.setVersion(project.getVersion());
        projectDTO.setCreatedDate(project.getCreatedDate());
        projectDTO.setCreatedBy(project.getCreatedBy());
        projectDTO.setUpdatedDate(project.getUpdatedDate());
        projectDTO.setUpdatedBy(project.getUpdatedBy());
        if (project.getTaggedUsers() != null) {
            List<UserInformationDTO> userDTOList = project.getTaggedUsers().stream().map(UserInformationSupport::convertUserEntityTOUserDTO).toList();
            projectDTO.setTaggedUsers(userDTOList);
        }
        return projectDTO;
    }


    public static ProjectInformation convertProjectDTOToProjectEntity(ProjectInformationDTO projectDTO) {
        ProjectInformation project = new ProjectInformation();
        project.setId(projectDTO.getId());
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setStatus(projectDTO.getStatus());
        project.setLanguage(projectDTO.getLanguage());
        project.setOwnerId(projectDTO.getOwnerId());
        project.setVersion(projectDTO.getVersion());
        project.setCreatedDate(projectDTO.getCreatedDate());
        project.setCreatedBy(projectDTO.getCreatedBy());
        project.setUpdatedDate(projectDTO.getUpdatedDate());
        project.setUpdatedBy(projectDTO.getUpdatedBy());
        if (projectDTO.getTaggedUsers() != null) {
            List<UserInformation> userList = projectDTO.getTaggedUsers().stream().map(UserInformationSupport::convertUsersDTOTOUsersEntity).toList();
            project.setTaggedUsers(userList);
        }
        return project;
    }

}