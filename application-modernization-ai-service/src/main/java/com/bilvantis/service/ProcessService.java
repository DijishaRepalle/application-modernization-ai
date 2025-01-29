package com.bilvantis.service;

import com.bilvantis.model.Process;
import com.bilvantis.model.ProcessSteps;
import com.bilvantis.model.ProcessTransaction;

import java.util.List;

public interface ProcessService {

    List<ProcessTransaction> fetchAllProjectScansOnJobId();

    void createProcessSchedule(String projectCode, String processName);

    List<ProcessTransaction> fetchAllProjectScansOnJobIdForProject(String projectCode);

    List<ProcessTransaction> fetchProcessTransactionsForJobId(String projectCode, String jobId);

    void createProcess(Process process);

    void createProcessSteps(List<ProcessSteps> processSteps);

    List<String> getProjectsByProcessId(String processId);

    List<ProcessTransaction> getTransactionsByProcessId(String processId);
}
