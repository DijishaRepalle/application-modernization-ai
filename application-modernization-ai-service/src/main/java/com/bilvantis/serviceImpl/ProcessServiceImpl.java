package com.bilvantis.serviceImpl;

import com.bilvantis.constants.ProcessErrorEnum;
import com.bilvantis.constants.Status;
import com.bilvantis.exception.ResourceNotFoundException;
import com.bilvantis.model.Process;
import com.bilvantis.model.ProcessSteps;
import com.bilvantis.model.ProcessTransaction;
import com.bilvantis.repository.ProcessRepository;
import com.bilvantis.repository.ProcessStepsRepository;
import com.bilvantis.repository.ProcessTransactionRepository;
import com.bilvantis.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoTemplate;

import static com.bilvantis.constants.CommonConstants.ERROR_EXCEPTION_LOG_PREFIX;
import static com.bilvantis.constants.CommonConstants.ERROR_LOG_PREFIX;

@Service
@Slf4j
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProcessStepsRepository processStepsRepository;

    @Autowired
    private ProcessTransactionRepository processTransactionRepository;

    @Override
    @Transactional
    public void createProcessSchedule(String projectCode, String processName) {

        List<ProcessSteps> processSteps = processStepsRepository.findByProcessName(processName);

        if (CollectionUtils.isEmpty(processSteps)) {
            log.error(ERROR_LOG_PREFIX, ProcessErrorEnum.PROCESS_STEPS_NOT_FOUND.getErrorId()
                    .concat(ProcessErrorEnum.PROCESS_STEPS_NOT_FOUND.getMessage()));
            throw new ResourceNotFoundException(String.format(ERROR_EXCEPTION_LOG_PREFIX,
                    ProcessErrorEnum.PROCESS_STEPS_NOT_FOUND.getErrorId()
                    .concat(ProcessErrorEnum.PROCESS_STEPS_NOT_FOUND.getMessage())));
        }
        List<ProcessTransaction> processTransactionSet = buildProcessTransactions(processSteps, projectCode);

        processTransactionRepository.saveAll(processTransactionSet);
    }
    @Override
    public List<ProcessTransaction> fetchAllProjectScansOnJobId() {
        List<ProcessTransaction> processTransactions = processTransactionRepository.findAll();

        Map<String, ProcessTransaction> groupedByJobId = processTransactions.stream()
                .collect(Collectors.toMap(
                        ProcessTransaction::getJobId,
                        transaction -> transaction,
                        (existing, replacement) -> existing // Keep the existing record if jobId is duplicated
                ));
        return new ArrayList<>(groupedByJobId.values());
    }
    @Override
    public List<ProcessTransaction> fetchAllProjectScansOnJobIdForProject(String projectCode) {
        List<ProcessTransaction> processTransactions = processTransactionRepository.findAllByProjectCode(projectCode);

        Map<String, ProcessTransaction> groupedByJobId = processTransactions.stream()
                .collect(Collectors.toMap(
                        ProcessTransaction::getJobId,
                        transaction -> transaction,
                        (existing, replacement) -> existing // Keep the existing record if jobId is duplicated
                ));
        return new ArrayList<>(groupedByJobId.values());
    }

    @Override
    public List<ProcessTransaction> fetchProcessTransactionsForJobId(String projectCode, String jobId) {

        return processTransactionRepository.findAllByProjectCodeAndJobId(projectCode, jobId, Sort.by(Sort.Direction.ASC, "stepId.stepSequence"));
    }

    @Override
    public void createProcess(Process process) {
        process.setProcessId(UUID.randomUUID().toString());
        processRepository.save(process);
    }

    @Override
    public void createProcessSteps(List<ProcessSteps> processSteps) {
        processSteps.forEach(processStep -> processStep.setStepId(UUID.randomUUID().toString()));
        processStepsRepository.saveAll(processSteps);
    }

    private List<ProcessTransaction> buildProcessTransactions(List<ProcessSteps> processSteps, String projectCode) {
        List<ProcessTransaction> processTransactionSet = new ArrayList<>();
        processSteps.forEach(processStep -> {
            ProcessTransaction processTransaction = new ProcessTransaction();
            processTransaction.setProcessTransactionId(UUID.randomUUID().toString());
            processTransaction.setJobId(generateJobId());
            if (processStep.getStepSequence() == 1) {
                processTransaction.setStatus(Status.TO_DO.getStatus());
            } else {
                processTransaction.setStatus(Status.OPEN.getStatus());
            }
            processTransaction.setTrial(0);
            processTransaction.setStepName(processStep.getStepName());
            processTransaction.setProjectCode(projectCode);
            processTransaction.setStepId(processStep.getStepId());
            processTransaction.setIsActive(Boolean.TRUE);
            processTransaction.setCreatedDate(LocalDateTime.now());
            processTransactionSet.add(processTransaction);
        });
        return processTransactionSet;
    }

    private String generateJobId() {
        StringBuilder jobId = new StringBuilder("JOB");
        processTransactionRepository.findTopByOrderByCreatedByDesc().ifPresentOrElse(
                transaction -> {
                    int seqNo = Integer.parseInt(transaction.getJobId().substring(3));
                    int newSeqNo = seqNo + 1;
                    jobId.append(newSeqNo);
                },
                () -> {
                    jobId.append("001");
                }
        );

        return jobId.toString();
    }
}
