package com.bilvantis.controller;


import com.bilvantis.config.CloneRequest;
import com.bilvantis.model.UserResponseDTO;
import com.bilvantis.serviceImpl.GitCloneService;
import com.bilvantis.util.UserRequestResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/api")
public class GitCloneController {
    @Autowired
    private GitCloneService gitCloneService;

    @PostMapping("/clone")
    public ResponseEntity<UserResponseDTO> cloneRepository(@RequestBody CloneRequest cloneRequest) throws IOException, InterruptedException {
     gitCloneService.cloneRepository(cloneRequest.getProjectCode(),cloneRequest.getToken());
        return ResponseEntity.ok(UserRequestResponseBuilder.buildResponseDTO(null, null, "Repository cloned successfully"));
    }

}
