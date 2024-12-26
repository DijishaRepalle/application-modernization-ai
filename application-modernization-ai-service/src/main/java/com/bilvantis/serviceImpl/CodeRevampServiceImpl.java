package com.bilvantis.serviceImpl;

import com.bilvantis.exception.ApplicationException;
import com.bilvantis.exception.ResourceNotFoundException;
import com.bilvantis.model.CodeRevamp;
import com.bilvantis.model.CodeRevampProcess;
import com.bilvantis.model.CodeRevampProcessSteps;
import com.bilvantis.model.CodeRevampTranscation;
import com.bilvantis.repository.CodeRevampProcessRepository;
import com.bilvantis.repository.CodeRevampProcessStepsRepository;
import com.bilvantis.repository.CodeRevampRepository;
import com.bilvantis.repository.CodeRevampTransactionRepository;
import com.bilvantis.service.CodeRevampService;
import com.bilvantis.util.AppModernizationProperties;
import com.bilvantis.util.ProcessErrorEnum;
import com.bilvantis.util.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bilvantis.constants.AppModernizationAPIConstants.JOB;
import static com.bilvantis.constants.AppModernizationAPIConstants.JOB_ID;
import static com.bilvantis.constants.CommonConstants.ERROR_EXCEPTION_LOG_PREFIX;
import static com.bilvantis.constants.CommonConstants.ERROR_LOG_PREFIX;

@Service
@Slf4j
public class CodeRevampServiceImpl implements CodeRevampService {

    private final CodeRevampRepository codeRevampRepository;

    private final AppModernizationProperties appModernizationProperties;

    private final CodeRevampTransactionRepository codeRevampTransactionRepository;
    private final CodeRevampProcessStepsRepository codeRevampProcessStepsRepository;
    private final CodeRevampProcessRepository codeRevampProcessRepository;

    public CodeRevampServiceImpl(CodeRevampRepository codeRevampRepository, AppModernizationProperties appModernizationProperties, CodeRevampTransactionRepository codeRevampTransactionRepository, CodeRevampProcessStepsRepository codeRevampProcessStepsRepository, CodeRevampProcessRepository codeRevampProcessRepository) {
        this.codeRevampRepository = codeRevampRepository;
        this.appModernizationProperties = appModernizationProperties;
        this.codeRevampTransactionRepository = codeRevampTransactionRepository;
        this.codeRevampProcessStepsRepository = codeRevampProcessStepsRepository;
        this.codeRevampProcessRepository = codeRevampProcessRepository;
    }

    /**
     * Creates a new code revamp process and saves it to the repository.
     *
     * @param codeRevamp the CodeRevamp object to be created
     * @throws ApplicationException if there is an error during the database operation
     */
    @Override
    public void createCodeRevampProcess(CodeRevamp codeRevamp) {
        try {
            codeRevamp.setCodeRevampId(UUID.randomUUID().toString());
            codeRevampRepository.save(codeRevamp);
        } catch (DataAccessException e) {
            log.error(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName(), e.getMessage());
            throw new ApplicationException(e.getMessage());
        }

    }

    /**
     * Creates new revamp process steps and saves them to the repository.
     *
     * @param codeRevamp the CodeRevampProcessSteps object to be created
     * @throws ApplicationException if there is an error during the database operation
     */
    @Override
    public void createRevampProcessSteps(CodeRevampProcessSteps codeRevamp) {
        try {
            codeRevamp.setCodeRevampStepId(UUID.randomUUID().toString());
            codeRevampProcessStepsRepository.save(codeRevamp);
        } catch (DataAccessException e) {
            log.error(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName(), e.getMessage());
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     * Creates a new revamp process and saves it to the repository.
     *
     * @param codeRevamp the CodeRevampProcess object to be created
     * @throws ApplicationException if there is an error during the database operation
     */
    @Override
    public void createRevampProcess(CodeRevampProcess codeRevamp) {
        try {
            codeRevamp.setCodeRevampProcessId(UUID.randomUUID().toString());
            codeRevampProcessRepository.save(codeRevamp);
        } catch (DataAccessException e) {
            log.error(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName(), e.getMessage());
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     * Creates a schedule for the code revamp process based on the project code and process name.
     *
     * @param projectCode the code of the project
     * @param processName the name of the process
     * @throws ResourceNotFoundException if the process steps are not found
     */

    @Override
    @Transactional
    public void createCodeRevampProcessSchedule(String projectCode, String processName) {
        List<CodeRevampProcessSteps> revampProcessSteps = codeRevampProcessStepsRepository.findByProcessName(processName);

        if (CollectionUtils.isEmpty(revampProcessSteps)) {
            log.error(ERROR_LOG_PREFIX, ProcessErrorEnum.PROCESS_STEPS_NOT_FOUND.getErrorId().concat(ProcessErrorEnum.PROCESS_STEPS_NOT_FOUND.getMessage()));
            throw new ResourceNotFoundException(String.format(ERROR_EXCEPTION_LOG_PREFIX, ProcessErrorEnum.PROCESS_STEPS_NOT_FOUND.getErrorId().concat(ProcessErrorEnum.PROCESS_STEPS_NOT_FOUND.getMessage())));
        }
        List<CodeRevampTranscation> codeRevampTranscationList = buildProcessTransactions(revampProcessSteps, projectCode);

        codeRevampTransactionRepository.saveAll(codeRevampTranscationList);
    }

    /**
     * Builds a list of process transactions based on the provided process steps and project code.
     *
     * @param codeRevampProcessSteps the list of process steps
     * @param projectCode            the code of the project
     * @return the list of CodeRevampTranscation objects
     * @throws ApplicationException if there is an error during the database operation
     */
    private List<CodeRevampTranscation> buildProcessTransactions(List<CodeRevampProcessSteps> codeRevampProcessSteps, String projectCode) {
        try {
            List<CodeRevampTranscation> codeRevampTranscationList = new ArrayList<>();
            codeRevampProcessSteps.forEach(processStep -> {
                CodeRevampTranscation codeRevampTranscation = new CodeRevampTranscation();
                codeRevampTranscation.setCodeRevampTransactionId(UUID.randomUUID().toString());
                codeRevampTranscation.setJobId(generateJobId());
                if (processStep.getStepSequence() == 1) {
                    codeRevampTranscation.setStatus(Status.TO_DO.getStatus());
                } else {
                    codeRevampTranscation.setStatus(Status.OPEN.getStatus());
                }
                codeRevampTranscation.setTrial(0);
                codeRevampTranscation.setStepName(processStep.getStepName());
                codeRevampTranscation.setProjectCode(projectCode);
                codeRevampTranscation.setCodeRevampStepId(processStep.getCodeRevampStepId());
                codeRevampTranscation.setIsActive(Boolean.TRUE);
                codeRevampTranscationList.add(codeRevampTranscation);
            });
            return codeRevampTranscationList;
        } catch (DataAccessException e) {
            log.error(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName(), e.getMessage());
            throw new ApplicationException(e.getMessage());

        }
    }

    /**
     * Generates a unique job ID for the code revamp transaction.
     *
     * @return the generated job ID
     * @throws ApplicationException if there is an error during the database operation
     */
    private String generateJobId() {
        try {
            StringBuilder jobId = new StringBuilder(JOB);
            codeRevampTransactionRepository.findLatestRecord().ifPresentOrElse(transaction -> {
                int seqNo = Integer.parseInt(transaction.getJobId().substring(3));
                int newSeqNo = seqNo + 1;
                jobId.append(newSeqNo);
            }, () -> {
                jobId.append(JOB_ID);
            });

            return jobId.toString();
        } catch (DataAccessException e) {
            log.error(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName(), e.getMessage());
            throw new ApplicationException(e.getMessage());

        }
    }
}