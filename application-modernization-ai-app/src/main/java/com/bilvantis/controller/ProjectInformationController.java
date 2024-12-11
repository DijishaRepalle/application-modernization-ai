package com.bilvantis.controller;

import com.bilvantis.model.ProjectInformationDTO;
import com.bilvantis.model.UserInformationDTO;
import com.bilvantis.service.ProjectInformationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project-service")
public class ProjectInformationController {

    private final ProjectInformationService projectInformationService;

    @Autowired
    public ProjectInformationController(ProjectInformationService projectInformationService) {
        this.projectInformationService = projectInformationService;
    }

    /**
     * Creates a new project with the provided project information.
     *
     * @param projectInformationDTO
     * @return A response containing the created project information
     */
    @PostMapping
    public ResponseEntity<ProjectInformationDTO> createProject(@Valid @NotNull @RequestBody ProjectInformationDTO projectInformationDTO) {
        ProjectInformationDTO saveProjectDetails = projectInformationService.createProject(projectInformationDTO);
        return new ResponseEntity<>(saveProjectDetails, HttpStatus.CREATED);
    }

    /**
     * Retrieves all project information.
     *
     * @return a list of all projects
     */
    @GetMapping("/project-list")
    public ResponseEntity<List<ProjectInformationDTO>> getAllProjects() {
        List<ProjectInformationDTO> getProjects = projectInformationService.getAllProjectInformation();
        return new ResponseEntity<>(getProjects, HttpStatus.OK);
    }


    /**
     * Deletes a project based on the provided project ID.
     *
     * @param id String
     */
    @DeleteMapping("/{id}")
    public void deleteProject(@NotNull @PathVariable String id) {
        projectInformationService.deleteProjectById(id);
    }


    /**
     * Updates the project information based on the provided project ID.
     *
     * @param id                    String
     * @param projectInformationDTO ProjectInformationDTO
     * @return the updated project information DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectInformationDTO> updateProject(@NotNull @PathVariable String id, @RequestBody ProjectInformationDTO projectInformationDTO) {
        ProjectInformationDTO updateData = projectInformationService.updateProjectByProjectId(id, projectInformationDTO);
        return new ResponseEntity<>(updateData, HttpStatus.OK);
    }

    /**
     * add users to particular project based on project code
     *
     * @param projectCode
     * @param userIds
     * @return list of users for project
     */
    @PutMapping("/{projectCode}/add-users")
    public ResponseEntity<ProjectInformationDTO> addUsersToProject(@PathVariable String projectCode, @RequestBody List<UserInformationDTO> userIds) {
        ProjectInformationDTO updatedProject = projectInformationService.addUsersToProject(projectCode, userIds);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    /**
     * Deletes the list of users tagged to particular project
     *
     * @param projectCode
     * @param userIds
     * @return deleted users
     */
    @DeleteMapping("/{projectCode}/delete-tagged-users")
    public ResponseEntity<ProjectInformationDTO> deleteTaggedUsers(@PathVariable String projectCode, @RequestBody List<String> userIds) {
        ProjectInformationDTO updatedProject = projectInformationService.removeTaggedUsersFromProject(projectCode, userIds);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }
}