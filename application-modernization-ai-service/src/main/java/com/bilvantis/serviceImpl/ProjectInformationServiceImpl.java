package com.bilvantis.serviceImpl;

import com.bilvantis.exception.ApplicationException;
import com.bilvantis.exception.DataNotFoundException;
import com.bilvantis.exception.ProjectImplementationSaveFailedException;
import com.bilvantis.model.ProjectInformation;
import com.bilvantis.model.ProjectInformationDTO;
import com.bilvantis.model.UserInformation;
import com.bilvantis.model.UserInformationDTO;
import com.bilvantis.repository.ProjectInformationRepository;
import com.bilvantis.repository.UserInformationRepository;
import com.bilvantis.service.ProjectInformationService;
import com.bilvantis.util.ProjectInformationSupport;
import com.bilvantis.util.UserInformationSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bilvantis.util.ProjectInformationServiceImplConstants.*;
import static com.bilvantis.util.ProjectInformationSupport.convertProjectDTOToProjectEntity;
import static com.bilvantis.util.ProjectInformationSupport.convertProjectEntityToProjectDTO;
import static com.bilvantis.util.UserInformationServiceImplConstants.*;

@Service
@Slf4j
public class ProjectInformationServiceImpl implements ProjectInformationService<ProjectInformation, ProjectInformationDTO> {
    private final ProjectInformationRepository projectInformationRepository;
    private final UserInformationRepository userInformationRepository;

    @Autowired
    public ProjectInformationServiceImpl(ProjectInformationRepository projectInformationRepository, UserInformationRepository userInformationRepository) {
        this.projectInformationRepository = projectInformationRepository;
        this.userInformationRepository = userInformationRepository;
    }


    @Override
    public ProjectInformationDTO createProject(ProjectInformationDTO projectInformationDTO) {
        try {
            if (ObjectUtils.isEmpty(projectInformationDTO)) {
                throw new DataNotFoundException(PROJECT_DETAILS_NOT_FOUND);
            }
            ProjectInformation saveProjectDetails = projectInformationRepository.save(convertProjectDTOToProjectEntity(projectInformationDTO));
            return convertProjectEntityToProjectDTO(saveProjectDetails);
        } catch (DataAccessException e) {
            log.error(EXCEPTION_ERROR_MESSAGE, this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName());
            throw new ProjectImplementationSaveFailedException(e.getMessage());
        }
    }

    @Override
    public List<ProjectInformationDTO> getAllProjectInformation() {
        try {
            List<ProjectInformation> listOfProjects = projectInformationRepository.findAll();
            if (CollectionUtils.isEmpty(listOfProjects)) {
                throw new DataNotFoundException(PROJECT_DETAILS_NOT_FOUND);
            }
            return listOfProjects.stream().map(ProjectInformationSupport::convertProjectEntityToProjectDTO).collect(Collectors.toList());

        } catch (DataAccessException e) {
            log.error(EXCEPTION_ERROR_MESSAGE, this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName());
            throw new ApplicationException(e.getMessage());
        }
    }

    @Override
    public void deleteProjectById(String projectId) {
        try {
            if (ObjectUtils.isEmpty(projectId)) {
                throw new DataNotFoundException(PROJECT_ID_NOT_FOUND);
            }
            Optional<ProjectInformation> projectInformation = projectInformationRepository.findById(projectId);
            if (projectInformation.isPresent()) {
                projectInformationRepository.deleteById(projectId);
            } else {
                throw new DataNotFoundException(PROJECT_ID_NOT_FOUND);
            }
        } catch (DataAccessException e) {
            log.error(EXCEPTION_ERROR_MESSAGE, this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName());
            throw new ApplicationException(e.getMessage());
        }
    }

    @Override
    public ProjectInformationDTO updateProjectByProjectId(String projectId, ProjectInformationDTO projectInfoDTO) {
        try {
            Optional<ProjectInformation> optionalProject = projectInformationRepository.findById(projectId);
            if (!optionalProject.isPresent()) {
                throw new DataNotFoundException(PROJECT_DETAILS_NOT_FOUND);
            }
            ProjectInformation projectInformation = optionalProject.get();
            projectInformation.setName(projectInfoDTO.getName());
            projectInformation.setLanguage(projectInfoDTO.getLanguage());
            ProjectInformation updatedProject = projectInformationRepository.save(projectInformation);
            return ProjectInformationSupport.convertProjectEntityToProjectDTO(updatedProject);
        } catch (DataAccessException e) {
            log.error(EXCEPTION_ERROR_MESSAGE, this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName());
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     * API to add  list of users to particular project
     *
     * @param projectCode
     * @param userDTOs
     * @return list of users
     */
    @Override
    public ProjectInformationDTO addUsersToProject(String projectCode, List<UserInformationDTO> userDTOs) {
        try {
            // Fetch the project by project code
            ProjectInformation project = projectInformationRepository.findByProjectCode(projectCode)
                    .orElseThrow(() -> new DataNotFoundException(PROJECT_DETAILS_NOT_FOUND));

            List<UserInformation> usersToAdd = new ArrayList<>();

            for (UserInformationDTO userDTO : userDTOs) {
                UserInformation user;
                if (userDTO.getId() != null) {
                    // Handle existing user
                    user = userInformationRepository.findById(userDTO.getId())
                            .orElseThrow(() -> new DataNotFoundException(USER_ID_NOT_FOUND));
                } else {
                    // Handle new user
                    user = UserInformationSupport.convertUsersDTOTOUsersEntity(userDTO);
                    userInformationRepository.save(user);
                }
                // Check for duplicate email in the project's tagged users
                if (project.getTaggedUsers() != null &&
                        project.getTaggedUsers().stream().anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()))) {
                    throw new ApplicationException(EMAIL_ALREADY_EXIST);
                }
                if (project.getTaggedUsers() != null && project.getTaggedUsers().stream().anyMatch(existingUser -> existingUser.getPhoneNumber().equals(user.getPhoneNumber()))) {
                    throw new ApplicationException(PHONE_NUMBER_ALREADY_EXIST);
                }
                usersToAdd.add(user);
            }
            // Add users to the project's tagged users list
            if (project.getTaggedUsers() == null) {
                project.setTaggedUsers(new ArrayList<>());
            }
            project.getTaggedUsers().addAll(usersToAdd);

            // Save the updated project
            ProjectInformation updatedProject = projectInformationRepository.save(project);

            return convertProjectEntityToProjectDTO(updatedProject);
        } catch (DataAccessException e) {
            log.error(EXCEPTION_ERROR_MESSAGE, this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName());
            throw new ProjectImplementationSaveFailedException(e.getMessage());
        }
    }

    /**
     * API to delete the tagged users for a particular project
     *
     * @param projectCode
     * @param userIds
     * @return users list
     */
    @Override
    public ProjectInformationDTO removeTaggedUsersFromProject(String projectCode, List<String> userIds) {
        try {
            // Fetch the project by project code
            ProjectInformation project = projectInformationRepository.findByProjectCode(projectCode).orElseThrow(() -> new DataNotFoundException(PROJECT_DETAILS_NOT_FOUND));

            // Ensure the project has tagged users
            if (project.getTaggedUsers() == null || project.getTaggedUsers().isEmpty()) {
                throw new ApplicationException(TAGGED_USERS_NOT_FOUND);
            }

            // Validate and remove users from the project's tagged users list
            List<UserInformation> taggedUsers = project.getTaggedUsers();

            for (String userId : userIds) {
                UserInformation userToRemove = taggedUsers.stream().filter(user -> user.getId().equals(userId)).findFirst().orElseThrow(() -> new ApplicationException(String.format(USER_ID_NOT_TAGGED_FOR_PROJECT, userId)));

                taggedUsers.remove(userToRemove);
            }
            // Save the updated project
            project.setTaggedUsers(taggedUsers);
            ProjectInformation updatedProject = projectInformationRepository.save(project);

            return convertProjectEntityToProjectDTO(updatedProject);
        } catch (DataAccessException e) {
            log.error(EXCEPTION_ERROR_MESSAGE, this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName());
            throw new ProjectImplementationSaveFailedException(e.getMessage());
        }
    }

}