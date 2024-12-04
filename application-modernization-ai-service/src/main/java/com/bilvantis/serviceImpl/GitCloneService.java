package com.bilvantis.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@Slf4j
public class GitCloneService {

    public String cloneRepository(String repoUrl, String token) {
        if (repoUrl == null || repoUrl.isEmpty()) {
            return "Repository URL is null or empty";
        }
        if (token == null || token.isEmpty()) {
            return "GitHub token is null or empty";
        }

        String cloneDirectoryPath = "src/main/clone/repo-clone";
        try {
            File cloneDirectory = new File(cloneDirectoryPath);
            if (!cloneDirectory.exists()) {
                cloneDirectory.mkdirs();
            }

            ProcessBuilder builder = new ProcessBuilder("git", "clone", "https://" + token + "@github.com/" + repoUrl, cloneDirectoryPath);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info(line);
                }
            }
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return "Repository cloned successfully";
            } else {
                return "Cloning failed with exit code " + exitCode;
            }
        } catch (IOException e) {
            return "An error occurred: " + e.getMessage();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "An error occurred: " + e.getMessage();
        } catch (Exception e) {
            return "An error occurred: " + e.getMessage();
        }
    }
}
