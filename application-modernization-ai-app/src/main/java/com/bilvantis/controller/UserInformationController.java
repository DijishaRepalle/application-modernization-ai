package com.bilvantis.controller;

import com.bilvantis.model.UserInformation;
import com.bilvantis.model.UserResponseDTO;
import com.bilvantis.service.UserInformationService;
import com.bilvantis.util.UserRequestResponseBuilder;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserInformationController {

    private final UserInformationService userInformationService;

    public UserInformationController(UserInformationService userInformationService) {
        this.userInformationService = userInformationService;
    }

    /**
     * Handles the creation of a new user. This method receives a request body containing user information,
     * passes it to the service layer for processing, and returns the created user.
     *
     * @param user The user information to be created. The request body must contain all necessary fields
     *             such as email, password, first name, last name, etc., which are used to create the user.
     *             The @RequestBody annotation ensures the incoming JSON data is mapped to a UserInformation object.
     * @return A ResponseEntity containing the created UserInformation object and an HTTP status code.
     *         If the user is successfully created, it returns HTTP 201 (Created).
     *         If there are validation issues or the user cannot be created, appropriate errors are handled at the service layer.
     */

    @Validated
    @PostMapping("/user")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserInformation user) {
        UserInformation createdUser = userInformationService.createUser(user);
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(createdUser,null,null), HttpStatus.CREATED);
    }

    /**
     * Retrieves a user based on the provided email address. This method processes a GET request to fetch
     * user information by email. If a user with the given email exists, it returns the user's details in the response.
     * If no user is found, it returns an HTTP 404 (Not Found).
     *
     * @param email The email address of the user to be fetched. The @RequestParam annotation binds the
     *              email query parameter from the URL to this method argument.
     * @return A ResponseEntity containing the UserInformation object if the user is found, and an HTTP status code.
     *         If the user is found, it returns HTTP 200 (OK) with the user data.
     *         If the user is not found, it returns HTTP 404 (Not Found).
     */
    @GetMapping("/fetch-by-email")
    public ResponseEntity<UserResponseDTO> fetchUser(@RequestParam String email) {
        Optional<UserInformation> fetchUser = userInformationService.fetchUser(email);
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(fetchUser,null,null), HttpStatus.CREATED);
    }

    /**
     * Updates the details of an existing user identified by the provided email address.
     * This method processes a PUT request to update the user information with the provided details.
     * If the user is found, their information is updated and the updated user details are returned in the response.
     * If the user is not found, the service layer should handle appropriate error handling or return null.
     *
     * @param email The email address of the user to be updated. This is provided as a query parameter
     *              in the URL and is used to locate the existing user.
     * @param updatedUserDetails The user information to be updated. The request body must contain the updated
     *                           user details such as first name, last name, password, etc. The @RequestBody
     *                           annotation ensures the incoming JSON data is mapped to the `UserInformation` object.
     * @return A ResponseEntity containing the updated `UserInformation` object and an HTTP status code.
     *         If the user is successfully updated, it returns HTTP 200 (OK) with the updated user data.
     *         If an error occurs, appropriate error handling is expected in the service layer.
     */

    @PutMapping("/user-update")
    public ResponseEntity<UserResponseDTO> updateUserInfo(@RequestParam String email, @RequestBody UserInformation updatedUserDetails) {
        UserInformation updatedUser = userInformationService.editUser(email, updatedUserDetails);
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(updatedUser, null,null),HttpStatus.OK);
    }

    /**
     * Deletes a user identified by the provided email address.
     * This method processes a DELETE request to remove a user from the system based on the provided email.
     * If the user exists, their information is deleted. If the user does not exist, the service layer should handle
     * the appropriate error or response.
     *
     * @param email The email address of the user to be deleted. This is provided as a query parameter in the request URL
     *              and is used to locate the user in the database.
     * @return A ResponseEntity indicating the result of the deletion operation.
     *         It returns HTTP 204 (No Content) when the user is successfully deleted, indicating that the request was
     *         processed successfully but no content is being returned in the response.
     *         If the user is not found, appropriate error handling should occur in the service layer.
     */

    @DeleteMapping("/user-delete")
    public ResponseEntity<Void> deleteUser(@RequestParam String email) {
        userInformationService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

}