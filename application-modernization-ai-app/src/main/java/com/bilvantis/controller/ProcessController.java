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

import static com.bilvantis.util.Status.SUCCESS;

@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;


    /**
     * Handles requests to create a process scan for a given project and process.
     *
     * @param projectCode the unique code of the project.
     * @param process the process name or identifier.
     * @return a ResponseEntity containing the status message and HTTP status code.
     */

    @PostMapping("/scan")
    public ResponseEntity<String> createProcessScan(@RequestParam("projectCode") String projectCode, @RequestParam("process") String process) {
        processService.createProcessSchedule(projectCode, process);
        return new ResponseEntity<>(SUCCESS.getStatus(), HttpStatus.CREATED);
    }

    /**
     * Fetches all unique project processes associated with a job ID.
     *
     * @return a ResponseEntity containing a list of ProcessTransaction objects and HTTP status code.
     */

    @GetMapping("/scan")
    public ResponseEntity<List<ProcessTransaction>> fetchProjectUniqueProcesses() {
        return new ResponseEntity<>(processService.fetchAllProjectScansOnJobId(), HttpStatus.OK);
    }

    /**
     * Fetches process transactions for a specific project and job ID.
     *
     * @param projectCode the unique code of the project.
     * @param jobId the unique identifier of the job.
     * @return a ResponseEntity containing a list of ProcessTransaction objects and HTTP status code.
     */

    @GetMapping("/scan-project")
    public ResponseEntity<List<ProcessTransaction>> fetchProcessTransactionsForJobId(@RequestParam("projectCode") String projectCode,
                                                                                     @RequestParam("jobId") String jobId) {
        return new ResponseEntity<>(processService.fetchProcessTransactionsForJobId(projectCode, jobId), HttpStatus.OK);
    }

    /**
     * Creates a new process.
     *
     * @param process the Process object containing the details of the process to be created.
     * @return a ResponseEntity containing the status message and HTTP status code.
     */

    @PostMapping
    public ResponseEntity<String> createProcess(@RequestBody Process process) {
        processService.createProcess(process);
        return new ResponseEntity<>(SUCCESS.getStatus(), HttpStatus.CREATED);
    }

    /**
     * Creates steps for a process.
     *
     * @param processSteps a list of ProcessSteps objects containing details of the steps to be created.
     * @return a ResponseEntity containing the status message and HTTP status code.
     */
    @PostMapping("/steps")
    public ResponseEntity<String> createProcessSteps(@RequestBody List<ProcessSteps> processSteps) {
        processService.createProcessSteps(processSteps);
        return new ResponseEntity<>(SUCCESS.getStatus(), HttpStatus.CREATED);
    }
}
