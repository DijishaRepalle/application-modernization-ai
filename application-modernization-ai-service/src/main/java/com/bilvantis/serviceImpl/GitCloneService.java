package com.bilvantis.serviceImpl;

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
import java.util.Optional;

import static com.bilvantis.util.AppModernizationAPIConstants.*;

@Service
@Slf4j
public class GitCloneService {

    @Autowired
    private CustomRepository customRepository;

    public void cloneRepository(String projectCode, String token) throws IOException, InterruptedException {
        // Validate project code
        validateProjectCode(projectCode);

        // Fetch project information
        ProjectInformation projectInformation = getProjectInformation(projectCode);

        // Fetch and validate repository URL
        String repoUrl = validateRepositoryUrl(projectInformation);

        // Generate the clone directory path
        String cloneDirectoryPath = generateCloneDirectoryPath(projectCode);

        // Clone the repository
        cloneRepositoryFromUrl(repoUrl, cloneDirectoryPath);
    }

    private void validateProjectCode(String projectCode) {
        if (StringUtils.isBlank(projectCode)) {
            throw new IllegalArgumentException(PROJECT_CODE_NOT_FOUND);
        }
    }

    private ProjectInformation getProjectInformation(String projectCode) {
        return customRepository.findByProjectCode(projectCode)
                .orElseThrow(() -> {
                    log.error(PROJECT_NOT_FOUND);
                    return new ResourceNotFoundException(PROJECT_NOT_FOUND);
                });
    }

    private String validateRepositoryUrl(ProjectInformation projectInformation) {
        String repoUrl = projectInformation.getRepoUrl();
        if (StringUtils.isBlank(repoUrl)) {
            log.error(INVALID_REPOSITORY);
            throw new InvalidRepositoryURLException(INVALID_REPOSITORY);
        }
        return repoUrl;
    }

    private String generateCloneDirectoryPath(String projectCode) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String timestamp = now.format(formatter);
        return CLONE_DIRECTORY + projectCode + HYPHEN + timestamp;
    }

    private void cloneRepositoryFromUrl(String repoUrl, String cloneDirectoryPath) throws IOException, InterruptedException {
        try {
            // Create the clone directory
            File cloneDirectory = new File(cloneDirectoryPath);
            if (!cloneDirectory.exists()) {
                cloneDirectory.mkdirs();
            }

            // Execute the git clone process
            ProcessBuilder builder = new ProcessBuilder("git", "clone", repoUrl, cloneDirectoryPath);
            builder.redirectErrorStream(true);
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
            if (exitCode != 0) {
                throw new CloneFailedException(CLONE_FAILED);
            }

        } catch (IOException | InterruptedException e) {
            log.error(ERROR_OCCURRED_WHILE_CLONING_PROJECT, e);
            throw new IOException(ERROR_OCCURRED_WHILE_CLONING_PROJECT, e);
        }
    }

}

