package com.bilvantis.controller;


import com.bilvantis.model.CloneRequest;
import com.bilvantis.model.UserResponseDTO;
import com.bilvantis.serviceImpl.GitCloneService;
import com.bilvantis.util.UserRequestResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.bilvantis.constants.AppModernizationAPIConstants.REPO_CLONE_SUCCESS;

@RestController
@Slf4j
@RequestMapping("/api")
public class GitCloneController {
    private final GitCloneService gitCloneService;

    @Autowired
    public GitCloneController(GitCloneService gitCloneService) {
        this.gitCloneService = gitCloneService;
    }

    /**
     * Handles the cloning of a Git repository.
     * This method accepts a request containing the project code and access token,
     * invokes the cloning functionality through the  gitCloneService,
     * and returns a success response.
     *
     * @param cloneRequest the request object containing the details required for cloning the repository,
     *                     including the project code and access token.
     * @return a ResponseEntity containing a  UserResponseDTO with a success message indicating the repository was cloned successfully.
     */
    @PostMapping("/clone")
    public ResponseEntity<UserResponseDTO> cloneRepository(@RequestBody CloneRequest cloneRequest) throws IOException, InterruptedException {
        gitCloneService.cloneRepository(cloneRequest.getProjectCode(), cloneRequest.getToken());
        return ResponseEntity.ok(UserRequestResponseBuilder.buildResponseDTO(null, null, REPO_CLONE_SUCCESS));
    }

}