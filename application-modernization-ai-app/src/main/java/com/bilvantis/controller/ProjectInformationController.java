package com.bilvantis.controller;

import com.bilvantis.model.ProjectInformationDTO;
import com.bilvantis.service.ProjectInformationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project-service")
public class ProjectInformationController {

    private final ProjectInformationService projectInformationService;

    public ProjectInformationController(ProjectInformationService projectInformationService) {
        this.projectInformationService = projectInformationService;
    }

    @PostMapping
    public ResponseEntity<ProjectInformationDTO> createProject(@Valid @NotNull @RequestBody ProjectInformationDTO projectInformationDTO) {
        ProjectInformationDTO saveProjectDetails = projectInformationService.createProject(projectInformationDTO);
        return new ResponseEntity<>(saveProjectDetails, HttpStatus.CREATED);
    }

    @GetMapping("/project-list")
    public ResponseEntity<List<ProjectInformationDTO>> getAllProjects() {
        List<ProjectInformationDTO> getProjects = projectInformationService.getAllProjectInformation();
        return new ResponseEntity<>(getProjects, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@NotNull @PathVariable String id) {
        projectInformationService.deleteProjectById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectInformationDTO> updateProject(@NotNull @PathVariable String id, @RequestBody ProjectInformationDTO projectInformationDTO) {
        ProjectInformationDTO updateData = projectInformationService.updateProjectByProjectId(id, projectInformationDTO);
        return new ResponseEntity<>(updateData, HttpStatus.OK);
    }

    @PutMapping("/{projectCode}/add-users")
    public ResponseEntity<ProjectInformationDTO> addUsersToProject(@PathVariable String projectCode, @RequestBody List<String> userIds) {
        ProjectInformationDTO updatedProject = projectInformationService.addUsersToProject(projectCode, userIds);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

}