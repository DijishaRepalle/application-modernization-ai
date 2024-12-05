package com.bilvantis.repositoryImpl;

import com.bilvantis.model.ProjectInformation;
import com.bilvantis.repository.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomRepositoryImpl implements CustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Optional<ProjectInformation> findByProjectCode(String projectCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("projectCode").is(projectCode));

        ProjectInformation projectInformation = mongoTemplate.findOne(query, ProjectInformation.class);
        return Optional.ofNullable(projectInformation);
    }


    @Override
    public String findHighestProjectCode() {
        Query query = new Query();
        query.with(Sort.by(Sort.Order.desc("projectCode")));
        query.limit(1);

        ProjectInformation highestProject = mongoTemplate.findOne(query, ProjectInformation.class);

        if (highestProject != null) {
            return highestProject.getProjectCode();
        } else {
            return "PRO0000";
        }
    }
}
