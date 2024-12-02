package com.bilvantis.repositoryImpl;

import com.bilvantis.model.UserInformation;
import com.bilvantis.repository.CustomUserInformationRepository;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CustomUserInformationRepositoryImpl implements CustomUserInformationRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Integer saveOtpAndOtpGenerationTime(String phoneNumber, String otp, Long otpGenerationTime) {
        Query query = new Query(Criteria.where("phoneNumber").is(phoneNumber));
        Update update = new Update().set("otp", otp).set("otpGenerationTime", otpGenerationTime);
        UpdateResult result = mongoTemplate.updateFirst(query, update, UserInformation.class);
        return Math.toIntExact(result.getModifiedCount());
    }


    @Override
    public String findRoleByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userId));
        UserInformation user = mongoTemplate.findOne(query, UserInformation.class);

        if (user != null) {
            return user.getRole();
        }
        return null;
    }
}
