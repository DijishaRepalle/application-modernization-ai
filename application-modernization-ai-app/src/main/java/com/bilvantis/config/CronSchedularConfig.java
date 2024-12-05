package com.bilvantis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@Slf4j
public class CronSchedularConfig {

    private static final String CLONE_DIRECTORY_BASE_PATH = "src/main/clone/";

    @Scheduled(cron = "0 * * * * *")
    public void cleanCloneDirectory() {
        try {
            File baseDir = new File(CLONE_DIRECTORY_BASE_PATH);
            if (baseDir.exists() && baseDir.isDirectory()) {
                File[] directories = baseDir.listFiles(File::isDirectory);
                if (directories != null) {
                    for (File dir : directories) {
                        deleteDirectory(dir.toPath());
                        System.out.println("Deleted directory: " + dir.getName());
                    }
                }
            } else {
                System.out.println("Base directory does not exist: " + CLONE_DIRECTORY_BASE_PATH);
            }
        } catch (Exception e) {
            System.err.println("Error while cleaning clone directory: " + e.getMessage());
        }
    }

    private void deleteDirectory(Path path) throws Exception {
        Files.walk(path)
                .sorted((path1, path2) -> path2.compareTo(path1))
                .map(Path::toFile)
                .forEach(File::delete);
    }
}
