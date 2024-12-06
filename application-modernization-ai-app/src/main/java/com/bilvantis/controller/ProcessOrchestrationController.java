package com.bilvantis.controller;

import com.bilvantis.model.UserResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.bilvantis.constants.Status.SUCCESS;

@RestController
@RequestMapping("/process")
public class ProcessOrchestrationController {

    @PostMapping
    public ResponseEntity<String> createProcess(@RequestParam("projectCode") String projectCode, @RequestParam("process") String process) {

        return new ResponseEntity<>(SUCCESS.getStatus(), HttpStatus.CREATED);
    }
}
