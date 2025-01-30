package com.bilvantis.repositoryImpl;

import com.bilvantis.exception.ResourceNotFoundException;
import com.bilvantis.model.UserInformation;
import com.bilvantis.repository.CustomUserInformationRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.bilvantis.constants.ProjectInformationServiceImplConstants.EXCEPTION_ERROR_MESSAGE;
import static com.bilvantis.constants.UserInformationRepositoryConstants.*;

@Repository
@Slf4j
public class CustomUserInformationRepositoryImpl implements CustomUserInformationRepository {
    private final MongoTemplate mongoTemplate;
    @Autowired
    public CustomUserInformationRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Integer saveOtpAndOtpGenerationTime(String phoneNumber, String otp, Long otpGenerationTime) {
        Query query = new Query(Criteria.where(PHONE_NUMBER).is(phoneNumber));
        Update update = new Update().set(OTP, otp).set(OTP_GEN_TIME, otpGenerationTime);
        UpdateResult result = mongoTemplate.updateFirst(query, update, UserInformation.class);
        return Math.toIntExact(result.getModifiedCount());
    }


    @Override
    public String findRoleByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ID).is(userId));
        UserInformation user = mongoTemplate.findOne(query, UserInformation.class);

        if (user != null) {
            return user.getRole();
        }
        return null;
    }


    /**
     * Retrieves a user from the database by their email address.
     * This method queries the database for a user with the specified email. If a user is found, the user information
     * is returned as an {@link Optional}. If no user is found, an empty {@link Optional} is returned.
     *
     * @param email The email address of the user to be retrieved. The email is used to locate the user in the database.
     *              It is assumed to be unique for each user.
     * @return An {@link Optional} containing the {@link UserInformation} object if the user exists in the database,
     * or an empty {@link Optional} if no user is found with the provided email.
     */

    @Override
    public Optional<UserInformation> findByEmail(String email) {
        UserInformation user = mongoTemplate.findOne(
                Query.query(Criteria.where(EMAIl).is(email)),
                UserInformation.class
        );
        return Optional.ofNullable(user);
    }


    /**
     * Updates the user information based on the provided email address.
     * This method updates the first name, last name, and the updated timestamp of the user
     * identified by the provided email address. If the user is found, the changes are applied
     * and the updated user information is returned.
     *
     * @param email The email address of the user whose information is to be updated. The email is used
     *              to find the user in the database.
     * @param updatedUserDetails The {@link UserInformation} object containing the new details
     *                           to be updated for the user (e.g., first name, last name).
     *                           The object should contain the updated values.
     * @return The updated {@link UserInformation} object reflecting the changes applied to the user's information.
     *         If no user is found with the provided email, this method returns {@code null}.
     */

    public UserInformation editUser(String email, UserInformation updatedUserDetails) {
        Update update = new Update()
                .set(FIRST_NAME, updatedUserDetails.getFirstName())
                .set(LAST_NAME, updatedUserDetails.getLastName())
                .set(UPDATED_AT, LocalDateTime.now());

        Query query = new Query(Criteria.where(EMAIl).is(email));

        // Execute the update based on email
        return mongoTemplate.findAndModify(
                query,
                update,
                UserInformation.class
        );
    }

    /**
     * Deletes a user from the database based on the provided email address.
     * This method attempts to find and delete a user document identified by the email address.
     * If the user is successfully deleted, no action is performed, but if the deletion fails (i.e., no user is found),
     * appropriate handling can be done in the future within the empty condition.
     * If any exception occurs during the deletion process, a runtime exception is thrown.
     *
     * @param email The email address of the user to be deleted. This is used to locate the user in the database.
     *              The email is expected to be unique for each user.
     * @throws RuntimeException if an error occurs while trying to delete the user, this exception includes the original
     *                          exception message and stack trace.
     */

    @Override
    public void delete(String email) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where(EMAIl).is(email));
            long deletedCount = mongoTemplate.remove(query, UserInformation.class).getDeletedCount();
            if (0 < deletedCount) {
                log.error(RECORDS_NOT_FOUND);
                throw new ResourceNotFoundException(RECORDS_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error(EXCEPTION_ERROR_MESSAGE, this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName());
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
