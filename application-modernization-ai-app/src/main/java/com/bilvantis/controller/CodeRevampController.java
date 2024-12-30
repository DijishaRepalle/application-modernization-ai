package com.bilvantis.controller;

import com.bilvantis.model.CodeRevampProcess;
import com.bilvantis.model.CodeRevampProcessSteps;
import com.bilvantis.service.CodeRevampService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilvantis.util.Status.SUCCESS;

@RestController
@RequestMapping("code_revamp")
public class CodeRevampController {

    private final CodeRevampService codeRevampService;


    public CodeRevampController(CodeRevampService codeRevampService) {
        this.codeRevampService = codeRevampService;
    }

    @PostMapping("/process")
    public ResponseEntity<String> createCodeRevampProcess(@RequestBody CodeRevampProcess codeRevamp) {
        codeRevampService.createRevampProcess(codeRevamp);
        return new ResponseEntity<>(SUCCESS.getStatus(), HttpStatus.CREATED);
    }

    @PostMapping("/steps")
    public ResponseEntity<String> createCodeRevampProcessSteps(@RequestBody List<CodeRevampProcessSteps> codeRevamp) {
        codeRevampService.createRevampProcessSteps(codeRevamp);
        return new ResponseEntity<>(SUCCESS.getStatus(), HttpStatus.CREATED);
    }

    @PostMapping("/code_revamp_process")
    public ResponseEntity<String> createCodeRevampProcess(@RequestParam("projectCode") String projectCode, @RequestParam("process") String process) {
        codeRevampService.createCodeRevampProcessSchedule(projectCode, process);
        return new ResponseEntity<>(SUCCESS.getStatus(), HttpStatus.CREATED);
    }
}
