package com.bilvantis.serviceImpl;

import com.bilvantis.exception.ApplicationException;
import com.bilvantis.exception.BadRequestException;
import com.bilvantis.exception.DataNotFoundException;
import com.bilvantis.exception.ProjectImplementationSaveFailedException;
import com.bilvantis.model.UserInformation;
import com.bilvantis.model.UserInformationDTO;
import com.bilvantis.repository.UserInformationRepository;
import com.bilvantis.service.UserInformationService;
import com.bilvantis.util.AppModernizationProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.bilvantis.util.ModernizationAppConstants.ROLE;
import static com.bilvantis.util.ProjectInformationServiceConstants.EXCEPTION_ERROR_MESSAGE;
import static com.bilvantis.util.ProjectInformationServiceConstants.PROJECT_DETAILS_NOT_FOUND;
import static com.bilvantis.util.UserInformationSupport.convertUserEntityTOUserDTO;
import static com.bilvantis.util.UserInformationSupport.convertUsersDTOTOUsersEntity;

@Service
@Slf4j
public class UserInformationServiceImpl implements UserInformationService<UserInformation, UserInformationDTO> {
    private final UserInformationRepository userInformationRepository;

    @Autowired
    AppModernizationProperties appModernizationProperties;


    public UserInformationServiceImpl(UserInformationRepository userInformationRepository) {
        this.userInformationRepository = userInformationRepository;
    }

    @Override
    public UserInformationDTO createProject(UserInformationDTO userInformationDTO) {
        try {
            if (ObjectUtils.isEmpty(userInformationDTO)) {
                throw new DataNotFoundException(PROJECT_DETAILS_NOT_FOUND);
            }
            UserInformation saveProjectDetails = userInformationRepository.save(convertUsersDTOTOUsersEntity(userInformationDTO));
            return convertUserEntityTOUserDTO(saveProjectDetails);
        } catch (DataAccessException e) {
            log.error(EXCEPTION_ERROR_MESSAGE, this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName());
            throw new ProjectImplementationSaveFailedException(e.getMessage());
        }
    }

    @Override
    public Boolean getRoleBasedOnUserId(String userId) {
        String role = userInformationRepository.findRoleByUserId(userId);
        return StringUtils.equalsIgnoreCase(role, ROLE);
    }

    /**
     * Creates a new user and saves it to the database.
     * This method sets the `createdAt` timestamp to the current time when the user is created and persists
     * the user information using the repository.
     *
     * @param user The user information to be saved. The input should be a `UserInformation` object with
     *             the necessary details such as email, first name, last name, and password.
     * @return The saved `UserInformation` object, including the assigned ID and the created timestamp.
     *         This object is returned after it has been persisted to the database.
     */
    @Override
    public UserInformation createUser(UserInformation user) {
        try {
            if (userInformationRepository.existsByEmail(user.getEmail())) {
                throw new BadRequestException("Email is already in use: " + user.getEmail());
            }
            user.setCreatedDate(LocalDateTime.now());
            return userInformationRepository.save(user);
        } catch (DataAccessException e) {
            log.error(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(),
                    e.getStackTrace()[0].getMethodName(), e.getMessage());
            throw new ApplicationException(e.getMessage());
        }
    }


    /**
     * Fetches a user based on the provided email address.
     * This method retrieves a user from the database using the provided email. It returns an {@link Optional}
     * containing the user information if the user is found, or an empty {@link Optional} if the user does not exist.
     *
     * @param email The email address of the user to be fetched. This is used to locate the user in the database.
     *              The email should be unique to each user.
     * @return An {@link Optional} containing the {@link UserInformation} object if the user is found,
     *         or an empty {@link Optional} if no user is found with the given email.
     */

    @Override
    public Optional<UserInformation> fetchUser(String email) {
        return userInformationRepository.findByEmail(email);
    }


    /**
     * Updates an existing user's information based on the provided email.
     * This method updates the user details (such as first name, last name, etc.)
     * using the provided email to identify the user and the {@link UserInformation} object to provide the updated values.
     *
     * @param email The email address of the user whose information needs to be updated.
     *              The email is used to locate the user in the database.
     * @param updatedUserDetails The updated user details, encapsulated in a {@link UserInformation} object.
     *                           This object contains the new values to be applied to the user.
     * @return The updated {@link UserInformation} object after the changes have been applied.
     *         This object reflects the most recent information in the database.
     */
    @Override
    public UserInformation editUser(String email, UserInformation updatedUserDetails) {
        return userInformationRepository.editUser(email, updatedUserDetails);
    }

    /**
     * Deletes a user identified by the provided email address.
     * This method removes the user from the database based on the provided email.
     * If the user exists, their information will be deleted. If the user does not exist, appropriate handling
     * should occur in the repository layer.
     *
     * @param email The email address of the user to be deleted. This is used to locate the user in the database
     *              and identify the record to be removed.
     */

    @Override
    public void deleteUser(String email) {
        userInformationRepository.delete(email);
    }


}
