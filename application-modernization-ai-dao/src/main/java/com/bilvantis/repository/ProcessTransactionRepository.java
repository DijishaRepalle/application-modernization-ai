package com.bilvantis.repository;

import com.bilvantis.model.ProcessTransaction;
import org.springframework.data.domain.Sort;import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcessTransactionRepository extends MongoRepository<ProcessTransaction, UUID> {
    //@Query(value = "{}", sort = "{ 'timestamp' : -1 }")
    Optional<ProcessTransaction> findTopByOrderByCreatedByDesc();
    @Query(value = "{}", sort = "{ 'timestamp' : -1 }")
    List<ProcessTransaction> findAllByProjectCode(String projectCode);

    @Query(value = "{}", sort = "{ 'timestamp' : -1 }")
    List<ProcessTransaction> findAll();
    List<ProcessTransaction> findAllByProjectCodeAndJobId(String projectCode, String jobId, Sort sort);

    List<ProcessTransaction> findByJobId(String stepName);
    List<ProcessTransaction> findDistinctProjectCodeByStepIdIn(List<String> stepIds);
    @Query("{ 'stepId': { $in: ?0 } }")
    List<ProcessTransaction> findByStepIdIn(List<String> stepIds);
    Optional<ProcessTransaction> findTopByJobIdStartingWithOrderByCreatedByDesc(String prefix);
    boolean existsByProjectCodeAndStepIdIn(String projectCode, List<String> stepIds);
}
