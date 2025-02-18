package com.bilvantis.util;

import com.bilvantis.repository.CustomRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.bilvantis.constants.AppModernizationAPIConstants.*;


@Component
public class ProjectCodeGenerator {

    private  static MongoTemplate mongoTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    private static CustomRepository customRepository;


    @Autowired
    public ProjectCodeGenerator(MongoTemplate mongoTemplate, CustomRepository customRepository) {
        ProjectCodeGenerator.mongoTemplate = mongoTemplate;
        ProjectCodeGenerator.customRepository = customRepository;
    }
    @PostConstruct
    public  void setMongoTemplate() {
        mongoTemplate = applicationContext.getBean(MongoTemplate.class);
    }

    /**
     * Generates a new project code based on the highest existing project code in the database.
     * <p>
     * The method retrieves the highest project code from the repository, extracts the numeric part,
     * increments it, and appends it to a predefined project code prefix. If no project codes exist,
     * it starts from 1.
     * </p>
     *
     * @return A newly generated project code in the format "{PROJECT_CODE}{4-digit number}".
     *         For example, if the highest code is "PROJ0005", the next code will be "PROJ0006".
     *         If no codes exist, the first code will be "PROJ0001".
     * @throws NumberFormatException if the numeric part of the highest code cannot be parsed.
     */

    public static String generateProjectCode() {
        String highestCode = customRepository.findHighestProjectCode();
        int currentMax = Optional.ofNullable(highestCode)
                .filter(code -> code.startsWith(PROJECT_CODE))
                .map(code -> Integer.parseInt(code.substring(PROJECT_CODE.length())))
                .orElse(ZERO);
        int newCode = currentMax + ONE;
        return PROJECT_CODE + String.format(PROJECT_NEW_CODE, newCode);
    }

}
