package com.bilvantis.controller;

import com.bilvantis.model.Process;
import com.bilvantis.model.ProcessSteps;
import com.bilvantis.model.ProcessTransaction;
import com.bilvantis.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilvantis.constants.Status.SUCCESS;

@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @PostMapping("/scan")
    public ResponseEntity<String> createProcessScan(@RequestParam("projectCode") String projectCode, @RequestParam("process") String process) {
        processService.createProcessSchedule(projectCode, process);
        return new ResponseEntity<>(SUCCESS.getStatus(), HttpStatus.CREATED);
    }

    @GetMapping("/scan")
    public ResponseEntity<List<ProcessTransaction>> fetchProjectUniqueProcesses(@RequestParam("projectCode") String projectCode) {
        return new ResponseEntity<>(processService.fetchAllProjectScansOnJobIdForProject(projectCode), HttpStatus.OK);
    }

    @GetMapping("/scan/project")
    public ResponseEntity<List<ProcessTransaction>> fetchProcessTransactionsForJobId(@RequestParam("projectCode") String projectCode,
                                                                                     @RequestParam("jobId") String jobId) {
        return new ResponseEntity<>(processService.fetchProcessTransactionsForJobId(projectCode, jobId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createProcess(@RequestBody Process process) {
        processService.createProcess(process);
        return new ResponseEntity<>(SUCCESS.getStatus(), HttpStatus.CREATED);
    }

    @PostMapping("/steps")
    public ResponseEntity<String> createProcessSteps(@RequestBody List<ProcessSteps> processSteps) {
        processService.createProcessSteps(processSteps);
        return new ResponseEntity<>(SUCCESS.getStatus(), HttpStatus.CREATED);
    }
}
