package com.bilvantis.serviceImpl;

import com.bilvantis.exception.ApplicationException;
import com.bilvantis.exception.CloneFailedException;
import com.bilvantis.exception.InvalidRepositoryURLException;
import com.bilvantis.exception.ResourceNotFoundException;
import com.bilvantis.model.ProjectInformation;
import com.bilvantis.repository.CustomRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.bilvantis.util.AppModernizationAPIConstants.*;

@Service
@Slf4j
public class GitCloneService {

    private final CustomRepository customRepository;

    @Autowired
    public GitCloneService(CustomRepository customRepository) {
        this.customRepository = customRepository;
    }

    /**
     * Clones a Git repository associated with the specified project code
     *
     * @param projectCode String
     * @param token       String
     * @throws IOException          if an I/O error occurs during the cloning process
     * @throws InterruptedException if the cloning process is interrupted
     */
    public void cloneRepository(String projectCode, String token) throws IOException, InterruptedException {
        // Validates the provided project code
        validateProjectCode(projectCode);
        //validates the token provided or not

        validateToken(token);
        // Fetches the project information using the project code
        ProjectInformation projectInformation = getProjectInformation(projectCode);

        // Retrieves and validates the repository URL from the project information
        String repoUrl = validateRepositoryUrl(projectInformation);

        // Generates a unique directory path for cloning the repository
        String cloneDirectoryPath = generateCloneDirectoryPath(projectCode);

        // Validates the provided token
        validateToken(token, projectInformation);

        // Executes the cloning process using the validated repository URL and generated path
        cloneRepositoryFromUrl(repoUrl, cloneDirectoryPath);
    }

    /**
     * validates based on project code if it ,exist or not
     *
     * @param projectCode String
     */
    private void validateProjectCode(String projectCode) {
        if (StringUtils.isBlank(projectCode)) {
            throw new ResourceNotFoundException(PROJECT_CODE_NOT_FOUND);
        }
    }
    /**
     * validates based on token if it ,exist or not
     * @param token String
     */
    private void validateToken(String token) {
        if (StringUtils.isBlank(token)) {
            throw new ResourceNotFoundException(TOKEN_NOT_FOUND);
        }
    }

    /**
     * validates the stored token and provide token  same or not
     * if equals it validates and clone the repository successfully
     * or else throw exception like invalid  token
     * @param token String
     * @param projectInformation ProjectInformation
     */
    private void validateToken(String token, ProjectInformation projectInformation) {
        // Fetch the token from the database
        String storedToken = projectInformation.getToken();
        // Compare the tokens
        if (!token.equals(storedToken)) {
            throw new ApplicationException(INVALID_TOKEN);
        }
    }

    /**
     * Fetch ProjectInformation based on given projectCode
     *
     * @param projectCode String
     * @return the ProjectInformation corresponding to the provided project code.
     */
    private ProjectInformation getProjectInformation(String projectCode) {
        return customRepository.findByProjectCode(projectCode).orElseThrow(() -> {
            log.error(PROJECT_NOT_FOUND);
            return new ResourceNotFoundException(PROJECT_NOT_FOUND);
        });
    }

    /**
     * Validates the repository URL for the given project.
     * <p>
     * This method retrieves the repository URL from the provided  ProjectInformation object
     * and checks if it is blank or invalid. If the repository URL is invalid, an error is logged,
     *
     * @param projectInformation the ProjectInformation object containing the repository details.
     * @return the valid repository URL as a String.
     */
    private String validateRepositoryUrl(ProjectInformation projectInformation) {
        String repoUrl = projectInformation.getRepoUrl();
        if (StringUtils.isBlank(repoUrl)) {
            log.error(INVALID_REPOSITORY);
            throw new InvalidRepositoryURLException(INVALID_REPOSITORY);
        }
        return repoUrl;
    }

    /**
     * Generates the directory path for cloning a repository.
     * <p>
     * This method creates a unique directory path by appending the project code and
     * a timestamp to a base clone directory. The timestamp is formatted using the
     *
     * @param projectCode String
     * @return the generated clone directory path as a String.
     */

    private String generateCloneDirectoryPath(String projectCode) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String timestamp = now.format(formatter);
        return CLONE_DIRECTORY + projectCode + HYPHEN + timestamp;
    }

    /**
     * Clones a Git repository from the specified URL into the given directory path.
     *
     * @param repoUrl            String
     * @param cloneDirectoryPath the local directory path where the repository will be cloned.
     * @throws IOException          if an I/O error occurs while cloning the repository.
     * @throws InterruptedException if the cloning process is interrupted.
     * @throws CloneFailedException if the Git clone process exits with a non-zero code.
     */
    private void cloneRepositoryFromUrl(String repoUrl, String cloneDirectoryPath) throws IOException, InterruptedException {
        try {
            //This method creates the necessary clone directory (if it does not exist)
            File cloneDirectory = new File(cloneDirectoryPath);
            if (!cloneDirectory.exists()) {
                cloneDirectory.mkdirs();
            }

            // Execute the git clone process
            ProcessBuilder builder = new ProcessBuilder(GIT, CLONE, repoUrl, cloneDirectoryPath);
            builder.redirectErrorStream(TRUE);
            Process process = builder.start();

            // Log the process output
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info(line);
                }
            }

            // Check the exit code and handle failure
            int exitCode = process.waitFor();
            if (exitCode != ZERO) {
                throw new CloneFailedException(CLONE_FAILED);
            }

        } catch (IOException | InterruptedException e) {
            log.error(ERROR_OCCURRED_WHILE_CLONING_PROJECT, e);
            throw new IOException(ERROR_OCCURRED_WHILE_CLONING_PROJECT, e);
        }
    }

}