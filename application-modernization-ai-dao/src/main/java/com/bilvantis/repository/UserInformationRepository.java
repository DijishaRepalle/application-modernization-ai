package com.bilvantis.repository;

import com.bilvantis.model.UserInformation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInformationRepository extends MongoRepository<UserInformation, String> {

}