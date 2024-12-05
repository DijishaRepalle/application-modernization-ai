package com.bilvantis.serviceImpl;

import com.bilvantis.model.ProjectInformation;
import com.bilvantis.repository.CustomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
public class GitCloneService {

    @Autowired
    private CustomRepository customRepository;

    public String cloneRepository(String projectCode, String token) {
        if (projectCode == null || projectCode.isEmpty()) {
            return "Project ID is null or empty";
        }

        Optional<ProjectInformation> projectInformation = customRepository.findByProjectCode(projectCode);

        if (projectInformation.isEmpty()) {
            return "Project with ID " + projectCode + " not found.";
        }

        String repoUrl = projectInformation.get().getRepoUrl();
        if (repoUrl == null || repoUrl.isEmpty()) {
            return "Repository URL is null or empty";
        }

        // Generate the clone directory path
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        String timestamp = now.format(formatter);
        String cloneDirectoryPath = "src/main/clone/" + projectCode + "-" + timestamp;

        try {
            File cloneDirectory = new File(cloneDirectoryPath);
            if (!cloneDirectory.exists()) {
                cloneDirectory.mkdirs();
            }

            // Construct the clone command with the token and URL from the project information
            ProcessBuilder builder = new ProcessBuilder("git", "clone", repoUrl, cloneDirectoryPath);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info(line); // Assuming you have a logger setup
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return "Repository cloned successfully to " + cloneDirectoryPath;
            } else {
                return "Cloning failed with exit code " + exitCode;
            }
        } catch (IOException | InterruptedException e) {
            return "An error occurred: " + e.getMessage();
        }
    }
}

