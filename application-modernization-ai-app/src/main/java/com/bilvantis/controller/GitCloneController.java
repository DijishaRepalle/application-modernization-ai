package com.bilvantis.controller;


import com.bilvantis.config.CloneRequest;
import com.bilvantis.serviceImpl.GitCloneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api")
public class GitCloneController {
    @Autowired
    private GitCloneService gitCloneService;

    @PostMapping("/clone")
    public String cloneRepository(@RequestBody CloneRequest cloneRequest) {
        try {
            return gitCloneService.cloneRepository(cloneRequest.getRepoUrl(), cloneRequest.getToken());
        } catch (Exception e) {
            return "An error occurred: " + e.getMessage();
        }
    }
}
