package com.bilvantis.repository;

import com.bilvantis.model.UserInformation;
import com.bilvantis.model.UserInformationDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInformationRepository extends MongoRepository<UserInformation, String> , CustomUserInformationRepository  {

    Optional<UserInformationDTO> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);


}