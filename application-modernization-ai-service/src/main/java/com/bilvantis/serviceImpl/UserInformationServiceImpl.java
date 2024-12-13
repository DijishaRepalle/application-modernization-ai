package com.bilvantis.serviceImpl;

import com.bilvantis.exception.ApplicationException;
import com.bilvantis.exception.BadRequestException;
import com.bilvantis.exception.DataNotFoundException;
import com.bilvantis.exception.ProjectImplementationSaveFailedException;
import com.bilvantis.model.ProjectInformation;
import com.bilvantis.model.ProjectInformationDTO;
import com.bilvantis.model.UserInformation;
import com.bilvantis.model.UserInformationDTO;
import com.bilvantis.repository.ProjectInformationRepository;
import com.bilvantis.repository.UserInformationRepository;
import com.bilvantis.service.UserInformationService;
import com.bilvantis.util.AppModernizationProperties;
import com.bilvantis.util.UserInformationSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.bilvantis.constants.AppModernizationAPIConstants.ROLE_NAME;
import static com.bilvantis.constants.ProjectInformationServiceImplConstants.*;
import static com.bilvantis.constants.UserInformationServiceImplConstants.*;

import static com.bilvantis.util.ProjectInformationSupport.convertProjectEntityToProjectDTO;

@Service
@Slf4j
public class UserInformationServiceImpl implements UserInformationService<UserInformation, UserInformationDTO> {
    private final UserInformationRepository userInformationRepository;
    private final ProjectInformationRepository projectInformationRepository;

    private final  AppModernizationProperties appModernizationProperties;


   @Autowired
   public UserInformationServiceImpl(UserInformationRepository userInformationRepository, ProjectInformationRepository projectInformationRepository, AppModernizationProperties appModernizationProperties) {
        this.userInformationRepository = userInformationRepository;
        this.projectInformationRepository = projectInformationRepository;
        this.appModernizationProperties = appModernizationProperties;
    }


    @Override
    public Boolean getRoleBasedOnUserId(String userId) {
        String role = userInformationRepository.findRoleByUserId(userId);
        return StringUtils.equalsIgnoreCase(role, ROLE_NAME);
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
                log.error(EMAIL_ALREADY_EXISTS);
                throw new BadRequestException(EMAIL_ALREADY_EXISTS);
            }
            user.setId(String.valueOf(UUID.randomUUID()));
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


    /**
     * to get the list of users present in the system
      * @return a list of users
     */
    @Override
    public List<UserInformationDTO> getAllUsersInformation() {
        try {
            List<UserInformation> listOfProjects = userInformationRepository.findAll();
            if (CollectionUtils.isEmpty(listOfProjects)) {
                log.error(USER_LIST_NOT_FOUND);
                throw new DataNotFoundException(USER_LIST_NOT_FOUND);
            }
            return listOfProjects.stream().map(UserInformationSupport::convertUserEntityTOUserDTO).collect(Collectors.toList());

        } catch (DataAccessException e) {
            log.error(EXCEPTION_ERROR_MESSAGE, this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName());
            throw new ApplicationException(e.getMessage());
        }
    }



    /**
     * API to tag  list of users to particular project
     *
     * @param projectCode String
     * @param userDTOs    UserInformationDTO
     * @return list of users tagged to project
     */
    @Override
    public ProjectInformationDTO addUsersToProject(String projectCode, List<UserInformationDTO> userDTOs) {
        try {
            // Fetch the project by project code
            ProjectInformation project = fetchProjectByProjectCode(projectCode);
            // Process and validate users
            List<UserInformation> usersToAdd = processUsers(userDTOs, project);

            // Add users to the project's tagged users list
            addUsersToTaggedList(project, usersToAdd);
            // Save the updated project
            ProjectInformation updatedProject = projectInformationRepository.save(project);

            return convertProjectEntityToProjectDTO(updatedProject);
        } catch (DataAccessException e) {
            log.error(EXCEPTION_ERROR_MESSAGE, this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName());
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     * API to delete the tagged users for a particular project
     *
     * @param projectCode String
     * @param userIds     String
     * @return users list
     */
    @Override
    public ProjectInformationDTO removeTaggedUsersFromProject(String projectCode, List<String> userIds) {
        try {
            // Fetch the project by project code
            ProjectInformation project = fetchProjectByProjectCode(projectCode);

            // Ensure the project has tagged users
            validateTaggedUsersExist(project);

            // Validate and remove users from the project's tagged users list
            List<UserInformation> taggedUsers = project.getTaggedUsers();
            for (String userId : userIds) {
                UserInformation userToRemove = taggedUsers.stream().filter(user -> user.getId().equals(userId))
                        .findFirst().orElseThrow(() ->
                                new ApplicationException(String.format(USER_ID_NOT_TAGGED_FOR_PROJECT, userId)));
                taggedUsers.remove(userToRemove);
            }
            // Save the updated project
            project.setTaggedUsers(taggedUsers);
            ProjectInformation updatedProject = projectInformationRepository.save(project);
            return convertProjectEntityToProjectDTO(updatedProject);
        } catch (DataAccessException e) {
            log.error(EXCEPTION_ERROR_MESSAGE, this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName());
            throw new ProjectImplementationSaveFailedException(e.getMessage());
        }
    }

    /**
     * validate method to check if the email exist or not
     *
     * @param project ProjectInformation
     * @param user   UserInformation
     */

    private void validateUserEmail(ProjectInformation project, UserInformation user) {
        if (project.getTaggedUsers() != null && project.getTaggedUsers().stream()
                .anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()))) {
            log.error(EMAIL_ALREADY_EXIST);
            throw new ApplicationException(EMAIL_ALREADY_EXIST);
        }
    }

    /**
     * validte method to check whether the phone number exist or not
     *
     * @param project UserInformation
     * @param user    UserInformation
     */
    private void validateUserPhoneNumber(ProjectInformation project, UserInformation user) {
        if (project.getTaggedUsers() != null && project.getTaggedUsers().stream()
                .anyMatch(existingUser -> existingUser.getPhoneNumber().equals(user.getPhoneNumber()))) {
            log.error(PHONE_NUMBER_ALREADY_EXIST);
            throw new ApplicationException(PHONE_NUMBER_ALREADY_EXIST);
        }
    }

    /**
     * get api to ensure weather the project exist or not
     *
     * @param projectCode String
     * @return projectCode
     */
    private ProjectInformation fetchProjectByProjectCode(String projectCode) {
        return projectInformationRepository.findByProjectCode(projectCode)
                .orElseThrow(() ->{
                    log.error(PROJECT_CODE_NOT_FOUND,projectCode);
                   throw new DataNotFoundException(PROJECT_CODE_NOT_FOUND);
                        });

    }

    /**
     * validates that the tagged user exist or not
     *
     * @param project ProjectInformation
     */
    private void validateTaggedUsersExist(ProjectInformation project) {
        if (project.getTaggedUsers() == null || project.getTaggedUsers().isEmpty()) {
            log.error(TAGGED_USERS_NOT_FOUND);
            throw new ApplicationException(TAGGED_USERS_NOT_FOUND);
        }
    }

    /**
     * Processes a list of user DTOs and converts them into a list of UserInformation objects.
     *
     * @param userDTOs UserInformationDTO
     * @param project  ProjectInformation
     * @return a list of UserInformation objects that have been created and validated.
     */
    private List<UserInformation> processUsers(List<UserInformationDTO> userDTOs, ProjectInformation project) {
        List<UserInformation> usersToAdd = new ArrayList<>();
        for (UserInformationDTO userDTO : userDTOs) {
            UserInformation user = CreateUser(userDTO);
            validateUserEmail(project, user);
            validateUserPhoneNumber(project, user);
            usersToAdd.add(user);
        }
        return usersToAdd;
    }

    /**
     * API to create new user if it does not exist if not the use existing user
     *
     * @param userDTO
     * @return user details
     */
    private UserInformation CreateUser(UserInformationDTO userDTO) {
        if (userDTO.getId() != null) {
            // fetch if there is an existing user
            return userInformationRepository.findById(userDTO.getId())
                    .orElseThrow(() -> {
                        log.error(USER_ID_NOT_FOUND);
                       throw new DataNotFoundException(USER_ID_NOT_FOUND);
                    });
        } else {
            //create new user
            UserInformation newUser = UserInformationSupport.convertUsersDTOTOUsersEntity(userDTO);
            return userInformationRepository.save(newUser);
        }
    }

    /**
     * It adds the users to tagged list of projects
     *
     * @param project    ProjectInformation
     * @param usersToAdd UserInformation
     */
    private void addUsersToTaggedList(ProjectInformation project, List<UserInformation> usersToAdd) {
        if (project.getTaggedUsers() == null) {
            project.setTaggedUsers(new ArrayList<>());
        }
        project.getTaggedUsers().addAll(usersToAdd);
    }


}
