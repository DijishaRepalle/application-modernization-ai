package com.bilvantis.serviceImpl;

import com.bilvantis.exception.ApplicationException;
import com.bilvantis.exception.DataNotFoundException;
import com.bilvantis.exception.ProjectImplementationSaveFailedException;
import com.bilvantis.model.ProjectInformation;
import com.bilvantis.model.ProjectInformationDTO;
import com.bilvantis.repository.ProjectInformationRepository;
import com.bilvantis.service.ProjectInformationService;
import com.bilvantis.util.ProjectInformationSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bilvantis.util.ProjectInformationServiceConstants.*;
import static com.bilvantis.util.ProjectInformationSupport.convertProjectDTOToProjectEntity;
import static com.bilvantis.util.ProjectInformationSupport.convertProjectEntityToProjectDTO;

@Service
@Slf4j
public class ProjectInformationServiceImpl implements ProjectInformationService<ProjectInformation, ProjectInformationDTO> {
    private final ProjectInformationRepository projectInformationRepository;

    public ProjectInformationServiceImpl(ProjectInformationRepository projectInformationRepository) {
        this.projectInformationRepository = projectInformationRepository;
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
}