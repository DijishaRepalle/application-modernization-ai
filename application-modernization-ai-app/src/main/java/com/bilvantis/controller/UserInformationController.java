package com.bilvantis.controller;

import com.bilvantis.model.UserInformationDTO;
import com.bilvantis.service.UserInformationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserInformationController {

    private final UserInformationService userInformationService;

    public UserInformationController(UserInformationService userInformationService) {
        this.userInformationService = userInformationService;
    }

    @PostMapping("/save-user")
    public ResponseEntity<UserInformationDTO> createProject(@Valid @NotNull @RequestBody UserInformationDTO projectInformationDTO) {
        UserInformationDTO saveProjectDetails = userInformationService.createProject(projectInformationDTO);
        return new ResponseEntity<>(saveProjectDetails, HttpStatus.CREATED);
    }

}