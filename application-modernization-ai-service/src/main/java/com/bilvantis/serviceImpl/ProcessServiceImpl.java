package com.bilvantis.serviceImpl;

import com.bilvantis.util.ProcessErrorEnum;
import com.bilvantis.util.Status;
import com.bilvantis.exception.ApplicationException;
import com.bilvantis.exception.ResourceNotFoundException;
import com.bilvantis.model.Process;
import com.bilvantis.model.ProcessSteps;
import com.bilvantis.model.ProcessTransaction;
import com.bilvantis.repository.ProcessRepository;
import com.bilvantis.repository.ProcessStepsRepository;
import com.bilvantis.repository.ProcessTransactionRepository;
import com.bilvantis.service.ProcessService;
import com.bilvantis.util.AppModernizationProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoTemplate;

import static com.bilvantis.constants.AppModernizationAPIConstants.*;
import static com.bilvantis.constants.CommonConstants.*;


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

    @Autowired
    AppModernizationProperties appModernizationProperties;

    /**
     * Creates a process schedule for the given project code and process name.
     * <p>
     * This method retrieves the process steps associated with the specified process name,
     * builds a list of {@link ProcessTransaction} objects, and saves them to the database.
     * If no process steps are found for the provided process name, it logs an error and
     * throws a {@link ResourceNotFoundException}.
     * </p>
     *
     * @param projectCode The unique identifier for the project to which the process schedule belongs.
     * @param processName The name of the process for which the schedule is to be created.
     * @throws ResourceNotFoundException If no process steps are found for the given process name.
     * @see ProcessStepsRepository#findByProcessName(String)
     */

    @Override
    @Transactional
    public void createProcessSchedule(String projectCode, String processName) {
        validateProcessNotInProgress(projectCode, processName);
        List<ProcessSteps> processSteps = processStepsRepository.findByProcessName(processName);

        if (CollectionUtils.isEmpty(processSteps)) {
            log.error(ERROR_LOG_PREFIX, ProcessErrorEnum.PROCESS_STEPS_NOT_FOUND.getErrorId()
                    .concat(ProcessErrorEnum.PROCESS_STEPS_NOT_FOUND.getMessage()));
            throw new ResourceNotFoundException(String.format(ERROR_EXCEPTION_LOG_PREFIX,
                    ProcessErrorEnum.PROCESS_STEPS_NOT_FOUND.getErrorId()
                            .concat(ProcessErrorEnum.PROCESS_STEPS_NOT_FOUND.getMessage())));
        }

        List<ProcessTransaction> processTransactionSet = buildProcessTransactions(processSteps, projectCode, processName);
        processTransactionRepository.saveAll(processTransactionSet);
    }

    private void validateProcessNotInProgress(String projectCode, String processName) {
        // Fetch all stepIds for the given processName
        List<String> stepIds = processStepsRepository.findByProcessName(processName)
                .stream()
                .map(ProcessSteps::getStepId)
                .collect(Collectors.toList());

        // Check if any ProcessTransaction exists for the given projectCode and stepIds
        boolean processInProgress = processTransactionRepository.existsByProjectCodeAndStepIdIn(projectCode, stepIds);

        if (processInProgress) {
            throw new ResourceNotFoundException(String.format(ERROR_EXCEPTION_LOG_PREFIX,
                    ProcessErrorEnum.PROCESS_ALREADY_IN_PROGRESS.getErrorId()
                            .concat(ProcessErrorEnum.PROCESS_ALREADY_IN_PROGRESS.getMessage())));
        }
    }

    /**
     * Fetches all project scans and returns a distinct list of {@link ProcessTransaction} objects grouped by their job IDs.
     * <p>
     * This method retrieves all process transactions from the database, groups them by their job IDs, and ensures
     * that only one transaction per job ID is included in the final list. If multiple transactions have the same job ID,
     * the first occurrence is retained.
     * </p>
     *
     * @return A distinct list of {@link ProcessTransaction} objects, each uniquely identified by its job ID.
     * @see ProcessTransactionRepository#findAll()
     */

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

    /**
     * Fetches all project scans for a given project code and returns a distinct list of {@link ProcessTransaction} objects grouped by their job IDs.
     * <p>
     * This method retrieves all process transactions associated with the specified project code from the database, groups them by their job IDs,
     * and ensures that only one transaction per job ID is included in the final list. If multiple transactions have the same job ID,
     * the first occurrence is retained.
     * </p>
     *
     * @param projectCode The unique code identifying the project whose transactions are to be fetched.
     * @return A distinct list of {@link ProcessTransaction} objects, each uniquely identified by its job ID, for the specified project code.
     * @see ProcessTransactionRepository#findAllByProjectCode(String)
     */

    @Override
    public List<ProcessTransaction> fetchAllProjectScansOnJobIdForProject(String projectCode) {
        try {
            List<ProcessTransaction> processTransactions = processTransactionRepository.findAllByProjectCode(projectCode);

            Map<String, ProcessTransaction> groupedByJobId = processTransactions.stream()
                    .collect(Collectors.toMap(
                            ProcessTransaction::getJobId,
                            transaction -> transaction,
                            (existing, replacement) -> existing // Keep the existing record if jobId is duplicated
                    ));
            return new ArrayList<>(groupedByJobId.values());
        } catch (DataAccessException e) {
            log.error(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(),
                    e.getStackTrace()[0].getMethodName(), e.getMessage());
            throw new ApplicationException(e.getMessage());

        }
    }

    /**
     * Fetches all {@link ProcessTransaction} records for a specific project code and job ID,
     * sorted by the step sequence in ascending order.
     * <p>
     * This method retrieves the list of process transactions that match the given project code
     * and job ID from the repository, and sorts the results based on the step sequence of the
     * associated  StepId in ascending order.
     * </p>
     *
     * @param projectCode The unique code identifying the project whose process transactions are to be fetched.
     * @param jobId The unique identifier for the job whose process transactions are to be retrieved.
     * @return A list of {@link ProcessTransaction} objects matching the specified project code and job ID, sorted by step sequence.
     * @see ProcessTransactionRepository#findAllByProjectCodeAndJobId(String, String, Sort)
     */

    @Override
    public List<ProcessTransaction> fetchProcessTransactionsForJobId(String projectCode, String jobId) {
      try{
          return processTransactionRepository.findAllByProjectCodeAndJobId(projectCode, jobId, Sort.by(Sort.Direction.ASC, "stepId.stepSequence"));

      }catch(DataAccessException e){
          log.error(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(),
                  e.getStackTrace()[0].getMethodName(), e.getMessage());
          throw new ApplicationException(e.getMessage());

      }
    }

    /**
     * Creates a new {@link Process} by generating a unique process ID and saving it to the repository.
     * <p>
     * This method sets a new unique process ID for the given {@link Process} object using a randomly generated UUID,
     * and then saves the process to the repository.
     * </p>
     *
     * @param process The {@link Process} object to be created and saved to the repository. The process ID will be automatically generated.
     */

    @Override
    public void createProcess(Process process) {
        try {
            process.setProcessId(UUID.randomUUID().toString());
            processRepository.save(process);
        }catch(DataAccessException e){
            log.error(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(),
                    e.getStackTrace()[0].getMethodName(), e.getMessage());
            throw new ApplicationException(e.getMessage());

        }
    }

    /**
     * Creates new {@link ProcessSteps} by generating a unique step ID for each and saving them to the repository.
     * <p>
     * This method iterates through the provided list of {@link ProcessSteps}, generates a unique UUID for each step
     * and sets it as the step ID, and then saves all the process steps to the repository.
     * </p>
     *
     * @param processSteps The list of {@link ProcessSteps} to be created. Each step will have a unique step ID generated.
     */

    @Override
    public void createProcessSteps(List<ProcessSteps> processSteps) {
        try{
            processSteps.forEach(processStep -> processStep.setStepId(UUID.randomUUID().toString()));
            processStepsRepository.saveAll(processSteps);
        }catch(DataAccessException e){
            log.error(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(),
                    e.getStackTrace()[0].getMethodName(), e.getMessage());
            throw new ApplicationException(e.getMessage());

        }
    }



    /**
     * Builds a list of {@link ProcessTransaction} objects based on the provided process steps and project code.
     * <p>
     * This method iterates through the list of {@link ProcessSteps}, creating a corresponding
     * {@link ProcessTransaction} for each step. Each transaction is initialized with a unique ID,
     * a generated job ID, status based on the step sequence, and other provided details.
     * </p>
     *
     * @param processSteps A list of {@link ProcessSteps} containing details for each process step.
     * @param projectCode The project code associated with the process transactions.
     * @return A list of {@link ProcessTransaction} objects representing the transactions for the provided process steps.
     */


    private List<ProcessTransaction> buildProcessTransactions(List<ProcessSteps> processSteps, String projectCode, String processName) {
        try {
            List<ProcessTransaction> processTransactionSet = new ArrayList<>();
            processSteps.forEach(processStep -> {
                ProcessTransaction processTransaction = new ProcessTransaction();
                processTransaction.setProcessTransactionId(UUID.randomUUID().toString());
                processTransaction.setJobId(generateJobId(processName));
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
                processTransactionSet.add(processTransaction);
            });
            return processTransactionSet;
        } catch (DataAccessException e) {
            log.error(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(),
                    e.getStackTrace()[0].getMethodName(), e.getMessage());
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     * Generates a unique job ID based on the latest record in the repository.
     * <p>
     * The method checks for the latest transaction record in the database to determine the next sequence number.
     * If a record is found, it extracts the sequence number from the existing job ID, increments it,
     * and appends it to a predefined job prefix. If no record exists, it starts with a default job ID.
     * </p>
     *
     * @return A newly generated job ID in the format "{JOB}{sequence number}".
     *         For example, if the latest job ID is "JOB123", the next ID will be "JOB124".
     *         If no records exist, it defaults to "JOB{JOB_ID}".
     * @throws NumberFormatException if the numeric part of the job ID cannot be parsed.
     */


    private String generateJobId(String processName) {
        try {
            // Get the prefix for the given processName
            String prefix = ProcessConstants.PROCESS_PREFIX_MAP.getOrDefault(processName, ProcessConstants.DEFAULT_PREFIX);

            StringBuilder jobId = new StringBuilder(prefix);

            processTransactionRepository.findTopByJobIdStartingWithOrderByCreatedByDesc(prefix).ifPresentOrElse(
                    transaction -> {
                        // Extract the numeric part of the job ID
                        String numericPart = transaction.getJobId().substring(prefix.length());
                        int seqNo = Integer.parseInt(numericPart);
                        int newSeqNo = seqNo + 1;
                        jobId.append(String.format("%03d", newSeqNo));
                    },
                    () -> {
                        jobId.append("001");
                    }
            );

            return jobId.toString();
        } catch (DataAccessException e) {
            log.error(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(),
                    e.getStackTrace()[0].getMethodName(), e.getMessage());
            throw new ApplicationException(e.getMessage());
        }
    }

        @Override
        public List<String> getProjectsByProcessId(String processId) {
            // Fetch all stepIds for the given processId
            List<String> stepIds = processStepsRepository.findByProcessName(processId)
                    .stream()
                    .map(ProcessSteps::getStepId)
                    .collect(Collectors.toList());

            // Fetch distinct projectCodes for the given stepIds
            List<ProcessTransaction> transactions = processTransactionRepository.findDistinctProjectCodeByStepIdIn(stepIds);

            // Extract and return unique projectCodes
            return transactions.stream()
                    .map(ProcessTransaction::getProjectCode)
                    .distinct()
                    .collect(Collectors.toList());
        }

    @Override
    public List<ProcessTransaction> getTransactionsByProcessId(String processId) {
        // Fetch all stepIds for the given processId
        List<ProcessSteps> processSteps = processStepsRepository.findByProcessName(processId);
        log.info("Fetched ProcessSteps for processId {}: {}", processId, processSteps);

        List<String> stepIds = processSteps.stream()
                .map(ProcessSteps::getStepId)
                .collect(Collectors.toList());
        log.info("Step IDs extracted: {}", stepIds);

        // Fetch all transactions for the given stepIds
        List<ProcessTransaction> transactions = processTransactionRepository.findByStepIdIn(stepIds);
        log.info("Fetched ProcessTransactions: {}", transactions);

        return transactions;
    }

    }

