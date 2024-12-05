package com.bilvantis.util;

import com.bilvantis.repository.CustomRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProjectCodeGenerator {

    private  static MongoTemplate mongoTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    private static CustomRepository customRepository;


    @Autowired
    public ProjectCodeGenerator(MongoTemplate mongoTemplate, CustomRepository customRepository) {
        this.mongoTemplate = mongoTemplate;
        this.customRepository = customRepository;
    }
    @PostConstruct
    public  void setMongoTemplate() {
        mongoTemplate = applicationContext.getBean(MongoTemplate.class);
    }

    public static String generateProjectCode() {
        String highestCode = customRepository.findHighestProjectCode();
        int currentMax = Integer.parseInt(highestCode.replace("PRO", ""));
        int newCode = currentMax + 1;
        return "PRO" + String.format("%04d", newCode);
    }

}
