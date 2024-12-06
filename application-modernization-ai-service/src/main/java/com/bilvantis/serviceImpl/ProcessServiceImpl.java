package com.bilvantis.serviceImpl;

import com.bilvantis.constants.ProcessErrorEnum;
import com.bilvantis.constants.Status;
import com.bilvantis.exception.ResourceNotFoundException;
import com.bilvantis.model.ProcessSteps;
import com.bilvantis.model.ProcessTransaction;
import com.bilvantis.repository.ProcessStepsRepository;
import com.bilvantis.repository.ProcessTransactionRepository;
import com.bilvantis.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.bilvantis.constants.CommonConstants.ERROR_EXCEPTION_LOG_PREFIX;
import static com.bilvantis.constants.CommonConstants.ERROR_LOG_PREFIX;

@Service
@Slf4j
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private ProcessStepsRepository processStepsRepository;

    @Autowired
    private ProcessTransactionRepository processTransactionRepository;

    @Override
    @Transactional
    public void createProcess(String projectCode, String processName) {

        Set<ProcessSteps> processSteps = processStepsRepository.findByProcessId_processName(processName);

        if (CollectionUtils.isEmpty(processSteps)) {
            log.error(ERROR_LOG_PREFIX, ProcessErrorEnum.PROCESS_STEPS_NOT_FOUND.getErrorId()
                    .concat(ProcessErrorEnum.PROCESS_STEPS_NOT_FOUND.getMessage()));
            throw new ResourceNotFoundException(String.format(ERROR_EXCEPTION_LOG_PREFIX,
                    ProcessErrorEnum.PROCESS_STEPS_NOT_FOUND.getErrorId()
                    .concat(ProcessErrorEnum.PROCESS_STEPS_NOT_FOUND.getMessage())));
        }
        Set<ProcessTransaction> processTransactionSet = buildProcessTransactions(processSteps, projectCode);

        processTransactionRepository.saveAll(processTransactionSet);
    }

    private Set<ProcessTransaction> buildProcessTransactions(Set<ProcessSteps> processSteps, String projectCode) {
        Set<ProcessTransaction> processTransactionSet = new HashSet<>();
        processSteps.forEach(processStep -> {
            ProcessTransaction processTransaction = new ProcessTransaction();
            processTransaction.setJobId(generateJobId());
            if (processStep.getStepSequence() == 1) {
                processTransaction.setStatus(Status.TO_DO.getStatus());
            } else {
                processTransaction.setStatus(Status.OPEN.getStatus());
            }
            processTransaction.setTrial(0);
            processTransaction.setProjectCode(projectCode);
            processTransaction.setStepId(processStep);
            processTransaction.setIsActive(Boolean.TRUE);
            processTransactionSet.add(processTransaction);
        });
        return processTransactionSet;
    }
    private String generateJobId() {
        StringBuilder jobId = new StringBuilder("JOB");
        processTransactionRepository.findLatestRecord().ifPresentOrElse(
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
