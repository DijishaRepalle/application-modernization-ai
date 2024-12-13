package com.bilvantis.serviceImpl;

import com.bilvantis.exception.ApplicationException;
import com.bilvantis.exception.DataNotFoundException;
import com.bilvantis.exception.ProjectImplementationSaveFailedException;
import com.bilvantis.model.ProjectInformation;
import com.bilvantis.model.ProjectInformationDTO;
import com.bilvantis.repository.ProjectInformationRepository;
import com.bilvantis.repository.UserInformationRepository;
import com.bilvantis.service.ProjectInformationService;
import com.bilvantis.util.ProjectInformationSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.bilvantis.constants.ProjectInformationServiceImplConstants.*;
import static com.bilvantis.util.ProjectInformationSupport.convertProjectDTOToProjectEntity;
import static com.bilvantis.util.ProjectInformationSupport.convertProjectEntityToProjectDTO;

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

    /**
     * creates a project with Project details
     *
     * @param projectInformationDTO ProjectInformationDTO
     * @return a response containing the created project information
     */

    @Override
    public ProjectInformationDTO createProject(ProjectInformationDTO projectInformationDTO) {
        try {
            if (ObjectUtils.isEmpty(projectInformationDTO)) {
                log.error(PROJECT_DETAILS_NOT_FOUND);
                throw new ProjectImplementationSaveFailedException(PROJECT_DETAILS_NOT_FOUND);
            }
            ProjectInformation saveProjectDetails = projectInformationRepository.save(convertProjectDTOToProjectEntity(projectInformationDTO));
            return convertProjectEntityToProjectDTO(saveProjectDetails);
        } catch (DataAccessException e) {
            log.error(EXCEPTION_ERROR_MESSAGE, this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName());
            throw new ProjectImplementationSaveFailedException(e.getMessage());
        }
    }

    /**
     * Retrieves all projects information.
     *
     * @return a list of all projects
     */
    @Override
    public List<ProjectInformationDTO> getAllProjectInformation() {
        try {
            List<ProjectInformation> listOfProjects = projectInformationRepository.findAll();
            if (CollectionUtils.isEmpty(listOfProjects)) {
                log.error(PROJECTS_LIST_NOT_FOUND);
                throw new DataNotFoundException(PROJECTS_LIST_NOT_FOUND);
            }

            // sorting the projects by alphabetical order
            listOfProjects.sort(Comparator.comparing(ProjectInformation::getProjectName));
            return listOfProjects.stream()
                    .map(ProjectInformationSupport::convertProjectEntityToProjectDTO)
                    .collect(Collectors.toList());

        } catch (DataAccessException e) {
            log.error(EXCEPTION_ERROR_MESSAGE, this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName(), e);
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     * Deletes a project based on the provided project ID.
     *
     * @param projectId String
     */
    @Override
    public void deleteProjectById(String projectId) {
        try {
            if (ObjectUtils.isEmpty(projectId)) {
                log.error(PROJECT_ID_NOT_NULL);
                throw new ApplicationException(PROJECT_ID_NOT_NULL);
            }
            ProjectInformation projectInformation = projectInformationRepository.findById(projectId).orElseThrow(() -> {
                log.error(PROJECT_ID_NOT_FOUND, projectId);
                throw  new DataNotFoundException(PROJECT_ID_NOT_FOUND);
            });

            projectInformationRepository.deleteById(projectId);

        } catch (DataAccessException e) {
            log.error(EXCEPTION_ERROR_MESSAGE, this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName());
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     * Updates the project information based on the provided project ID.
     *
     * @param projectId      String
     * @param projectInfoDTO ProjectInformationDTO
     * @return the updated project information DTO
     */
    @Override
    public ProjectInformationDTO updateProjectByProjectId(String projectId, ProjectInformationDTO projectInfoDTO) {
        try {
            ProjectInformation projectInformation = projectInformationRepository.findById(projectId).orElseThrow(() ->
            {
                log.error(PROJECT_ID_NOT_FOUND,projectId);
                throw  new DataNotFoundException(PROJECT_ID_NOT_FOUND);
            });
            projectInformation.setProjectName(projectInfoDTO.getProjectName());
            projectInformation.setProgrammingLanguage(projectInfoDTO.getProgrammingLanguage());
            ProjectInformation updatedProject = projectInformationRepository.save(projectInformation);
            return ProjectInformationSupport.convertProjectEntityToProjectDTO(updatedProject);
        } catch (DataAccessException e) {
            log.error(EXCEPTION_ERROR_MESSAGE, this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName());
            throw new ApplicationException(e.getMessage());
        }
    }
}